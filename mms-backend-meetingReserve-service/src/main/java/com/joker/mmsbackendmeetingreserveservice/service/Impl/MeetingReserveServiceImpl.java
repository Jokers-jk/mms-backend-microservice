package com.joker.mmsbackendmeetingreserveservice.service.Impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmeetingreserveservice.mapper.MeetingReserveMapper;
import com.joker.mmsbackendmeetingreserveservice.rabbitmq.MyMessageProducer;
import com.joker.mmsbackendmeetingreserveservice.service.MeetingReserveService;
import com.joker.mmsbackendmodel.dto.meeting.MeetingQueryRequest;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveAddRequest;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveQueryRequest;
import com.joker.mmsbackendmodel.entity.Meeting;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.enums.MeetingReserveStatusEnum;
import com.joker.mmsbackendmodel.vo.MeetingReserveVO;
import com.joker.mmsbackendmodel.vo.MeetingVO;
import com.joker.mmsbackendmodel.vo.UserVO;
import com.joker.mmsbackendserviceclient.service.MeetingFeignClient;
import com.joker.mmsbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议室预定管理
 */
@Service
@Slf4j
public class MeetingReserveServiceImpl extends ServiceImpl<MeetingReserveMapper, MeetingReserve> implements MeetingReserveService {

    @Resource
    private MeetingFeignClient meetingFeignClient;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;





    @Override
    public QueryWrapper<MeetingReserve> getQueryWrapper(MeetingReserveQueryRequest meetingReserveQueryRequest) {
        QueryWrapper<MeetingReserve> queryWrapper = new QueryWrapper<>();
        if(meetingReserveQueryRequest == null){
            return queryWrapper;
        }

        Long meetingId = meetingReserveQueryRequest.getMeetingId();
        String meetingTheme = meetingReserveQueryRequest.getMeetingTheme();
        Date meetingStartTime = meetingReserveQueryRequest.getMeetingStartTime();
        Date meetingEndTime = meetingReserveQueryRequest.getMeetingEndTime();
        Long userId = meetingReserveQueryRequest.getUserId();
        Integer meetingReserveStatus = meetingReserveQueryRequest.getMeetingReserveStatus();

        String sortField = meetingReserveQueryRequest.getSortField();
        String sortOrder = meetingReserveQueryRequest.getSortOrder();

        queryWrapper.ge(ObjectUtils.isNotEmpty(meetingStartTime), "meetingStartTime",meetingStartTime);
        queryWrapper.le(ObjectUtils.isNotEmpty(meetingEndTime), "meetingEndTime",meetingEndTime);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"userId",userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(meetingReserveStatus),"meetingReserveStatus",meetingReserveStatus);

        queryWrapper.eq(ObjectUtils.isNotEmpty(meetingId), "meetingId", meetingId);
        queryWrapper.like(StringUtils.isNotBlank(meetingTheme),"meetingTheme",meetingTheme);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;

    }

    @Override
    public QueryWrapper<MeetingReserve> getMyQueryWrapper(MeetingReserveQueryRequest meetingReserveQueryRequest) {
        QueryWrapper<MeetingReserve> queryWrapper = new QueryWrapper<>();
        if(meetingReserveQueryRequest == null){
            return queryWrapper;
        }

        Long meetingId = meetingReserveQueryRequest.getMeetingId();
        String meetingTheme = meetingReserveQueryRequest.getMeetingTheme();
        Date meetingStartTime = meetingReserveQueryRequest.getMeetingStartTime();
        Date meetingEndTime = meetingReserveQueryRequest.getMeetingEndTime();
        Integer meetingReserveStatus = meetingReserveQueryRequest.getMeetingReserveStatus();
        List<Long> userIdList = meetingReserveQueryRequest.getMeetingParticipants();



        String sortField = meetingReserveQueryRequest.getSortField();
        String sortOrder = meetingReserveQueryRequest.getSortOrder();

        for(Long userId : userIdList){
            queryWrapper.like("meetingParticipants", "," + userId + ",").or().like("meetingParticipants", "[" + userId + ",")
                    .or().like("meetingParticipants", "," + userId + "]").or().like("meetingParticipants", "[" + userId + "]");
        }



        queryWrapper.ge(ObjectUtils.isNotEmpty(meetingStartTime), "meetingStartTime",meetingStartTime);
        queryWrapper.le(ObjectUtils.isNotEmpty(meetingEndTime), "meetingEndTime",meetingEndTime);
        queryWrapper.eq(ObjectUtils.isNotEmpty(meetingReserveStatus),"meetingReserveStatus",meetingReserveStatus);

        queryWrapper.eq(ObjectUtils.isNotEmpty(meetingId), "meetingId", meetingId);
        queryWrapper.like(StringUtils.isNotBlank(meetingTheme),"meetingTheme",meetingTheme);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public Page<MeetingReserveVO> getMeetingReserveVOPage(Page<MeetingReserve> meetingReservePage) {
        List<MeetingReserve> meetingReserveList = meetingReservePage.getRecords();
        Page<MeetingReserveVO> meetingReserveVOPage = new Page<>(meetingReservePage.getCurrent(), meetingReservePage.getSize(), meetingReservePage.getTotal());
        if (CollUtil.isEmpty(meetingReserveList)) {
            return  meetingReserveVOPage;
        }
        List<MeetingReserveVO> meetingReserveVOList = meetingReserveList.stream().map(meetingReserve ->{
            return getMeetingReserveVO(meetingReserve);
        }).collect(Collectors.toList());
        meetingReserveVOPage.setRecords(meetingReserveVOList);
        return meetingReserveVOPage;
    }

    @Override
    public MeetingReserveVO getMeetingReserveVO(MeetingReserve meetingReserve) {
        MeetingReserveVO meetingReserveVO = MeetingReserveVO.objToVo(meetingReserve);
        Meeting meeting = meetingFeignClient.getById(meetingReserve.getMeetingId());
        MeetingVO meetingVO = meetingFeignClient.getMeetingVO(meeting);
        meetingReserveVO.setMeetingVO(meetingVO);

        List<Long> meetingParticipantsList = meetingReserveVO.getMeetingParticipantsList();
        if(CollUtil.isEmpty(meetingParticipantsList)){
            return meetingReserveVO;
        }

        List<UserVO> userVOList = meetingParticipantsList.stream().map(userId ->{
            return userFeignClient.getUserVO(userId);
        }).collect(Collectors.toList());
        meetingReserveVO.setUserVOList(userVOList);
        return meetingReserveVO;

    }

    @Override
    public Long doMeetingReserve(MeetingReserveAddRequest meetingReserveAddRequest, User user) {
        List<Long> participants = meetingReserveAddRequest.getMeetingParticipants();
        Long meetingId = meetingReserveAddRequest.getMeetingId();
        Date meetingStartTime = meetingReserveAddRequest.getMeetingStartTime();
        Date meetingEndTime = meetingReserveAddRequest.getMeetingEndTime();
        String theme = meetingReserveAddRequest.getMeetingTheme();
        if(participants == null){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"没有选择会议参加人员");
        }
        if(meetingId == null){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"没有选择会议室");
        }
        if(meetingStartTime == null){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"没有选择会议开始时间");
        }
        if(meetingEndTime == null){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"没有选择会议结束时间");
        }
        if(theme == null){
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"没有添加会议主题");
        }
        MeetingReserve meetingReserve = new MeetingReserve();
        meetingReserve.setUserId(user.getId());
        meetingReserve.setMeetingReserveStatus(MeetingReserveStatusEnum.WAITING.getValue());
        BeanUtils.copyProperties(meetingReserveAddRequest,meetingReserve);
        meetingReserve.setMeetingParticipants(meetingReserveAddRequest.getMeetingParticipants().toString());
        meetingReserve.setParticipateNumber((long)participants.size());
        boolean save = this.save(meetingReserve);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long meetingReserveId = meetingReserve.getId();
        myMessageProducer.sendMessage("meetingReserve_exchange","my_routingKey",String.valueOf( meetingReserveId));
        return  meetingReserveId;
    }
}

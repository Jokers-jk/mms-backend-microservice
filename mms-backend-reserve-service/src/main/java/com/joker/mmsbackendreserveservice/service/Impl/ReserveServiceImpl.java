package com.joker.mmsbackendreserveservice.service.Impl;

import cn.hutool.core.util.ObjectUtil;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.exception.ThrowUtils;
import com.joker.mmsbackendreserveservice.service.ReserveService;
import com.joker.mmsbackendmodel.dto.meeting.MeetingQueryRequest;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveQueryRequest;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import com.joker.mmsbackendmodel.enums.MeetingReserveStatusEnum;
import com.joker.mmsbackendmodel.enums.MeetingStatusEnum;
import com.joker.mmsbackendmodel.vo.MeetingReserveVO;
import com.joker.mmsbackendserviceclient.service.MeetingFeignClient;
import com.joker.mmsbackendserviceclient.service.MeetingReserveFeignClient;
import com.joker.mmsbackendserviceclient.service.UserFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class ReserveServiceImpl implements ReserveService {


    @Resource
    private MeetingFeignClient meetingFeignClient;

    @Resource
    private UserFeignClient userFeignClient;


    @Resource
    private MeetingReserveFeignClient meetingReserveFeignClient;
    @Override
    public MeetingReserve doMeetingReserve(Long meetingReserveId) {

        //提交信息是否存在
        MeetingReserve meetingReserve = meetingReserveFeignClient.getMeetingReserveById(meetingReserveId);
        if(ObjectUtil.isEmpty(meetingReserve)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }

        //是否为等待中
        if(!meetingReserve.getMeetingReserveStatus().equals(MeetingReserveStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"会议室预定信息正在处理");
        }

        MeetingReserve meetingReserveUpdate = new MeetingReserve();
        meetingReserveUpdate.setId(meetingReserveId);
        meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.RUNNING.getValue());
        boolean a = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
        ThrowUtils.throwIf(!a,ErrorCode.SYSTEM_ERROR);

        //会议室是否存在
        MeetingQueryRequest meetingQueryRequest = new MeetingQueryRequest();
        meetingQueryRequest.setMeetingStatus(MeetingStatusEnum.READY.getValue());
        meetingQueryRequest.setId(meetingReserve.getMeetingId());
        boolean b = meetingFeignClient.isExist(meetingQueryRequest);
        if(!b){
            meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.FAILED.getValue());
            meetingReserveUpdate.setMeetingSummary("会议室不存在或会议室维护中");
            boolean c = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
            ThrowUtils.throwIf(!c,ErrorCode.SYSTEM_ERROR);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"会议室不存在或会议室维护中");
        }

        //与会人员是否存在
        MeetingReserveVO meetingReserveVO = MeetingReserveVO.objToVo(meetingReserve);
        List<Long> participants = meetingReserveVO.getMeetingParticipantsList();
        boolean userExist = true;
        for(Long participant : participants) {
            if (userFeignClient.getById(participant) == null) {
                userExist = false;
                break;
            }
        }
        if(!userExist){
            meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.FAILED.getValue());
            meetingReserveUpdate.setMeetingSummary("会议发起人或与会人员不存在");
            boolean c = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
            ThrowUtils.throwIf(!c,ErrorCode.SYSTEM_ERROR);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"会议发起人或与会人员不存在");
        }



        //时间非法或时间被占用
        Date currentDate = new Date();
        boolean timeLegal = meetingReserve.getMeetingStartTime().after(currentDate) && meetingReserve.getMeetingEndTime().after(meetingReserve.getMeetingStartTime());
        if(!timeLegal){
            meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.FAILED.getValue());
            meetingReserveUpdate.setMeetingSummary("时间非法");
            boolean c = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
            ThrowUtils.throwIf(!c,ErrorCode.SYSTEM_ERROR);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"时间非法");
        }

        //会议室被占用
        MeetingReserveQueryRequest meetingReserveQueryRequest = new MeetingReserveQueryRequest();
        meetingReserveQueryRequest.setMeetingReserveStatus(MeetingReserveStatusEnum.SUCCEED.getValue());
        meetingReserveQueryRequest.setMeetingId(meetingReserve.getMeetingId());
        meetingReserveQueryRequest.setMeetingStartTime(meetingReserve.getMeetingStartTime());
        meetingReserveQueryRequest.setMeetingEndTime(meetingReserve.getMeetingEndTime());
        boolean d = meetingReserveFeignClient.isExist(meetingReserveQueryRequest);
        if(d){
            meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.FAILED.getValue());
            meetingReserveUpdate.setMeetingSummary("该时间段会议室已被占用");
            boolean c = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
            ThrowUtils.throwIf(!c,ErrorCode.SYSTEM_ERROR);
            throw new BusinessException(ErrorCode.REQUEST_ERROR,"该时间段会议室已被占用");
        }

        //会议室预定成功
        meetingReserveUpdate.setMeetingReserveStatus(MeetingReserveStatusEnum.SUCCEED.getValue());
        boolean c = meetingReserveFeignClient.updateMeetingReserveById(meetingReserveUpdate);
        ThrowUtils.throwIf(!c,ErrorCode.SYSTEM_ERROR,"会议室预定状态更新错误");
        MeetingReserve meetingReserveResult = meetingReserveFeignClient.getMeetingReserveById(meetingReserveId);
        return meetingReserveResult;
    }


}

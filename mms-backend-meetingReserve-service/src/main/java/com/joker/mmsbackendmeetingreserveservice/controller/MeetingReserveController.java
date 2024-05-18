package com.joker.mmsbackendmeetingreserveservice.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joker.mmsbackendcommon.annotation.AuthCheck;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.common.ResultUtils;
import com.joker.mmsbackendcommon.constant.UserConstant;
import com.joker.mmsbackendcommon.exception.ThrowUtils;
import com.joker.mmsbackendmeetingreserveservice.service.MeetingReserveService;
import com.joker.mmsbackendmodel.dto.meeting.*;

import com.joker.mmsbackendmodel.dto.meetingreserve.*;
import com.joker.mmsbackendmodel.entity.Meeting;
import com.joker.mmsbackendmodel.vo.MeetingReserveVO;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import com.joker.mmsbackendmodel.enums.MeetingStatusEnum;
import com.joker.mmsbackendcommon.common.BaseResponse;
import com.joker.mmsbackendmodel.enums.UserRoleEnum;
import com.joker.mmsbackendserviceclient.service.MeetingFeignClient;
import com.joker.mmsbackendserviceclient.service.UserFeignClient;
import com.joker.mmsbackendcommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import com.joker.mmsbackendmodel.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin
public class MeetingReserveController {

    @Resource
    private MeetingReserveService meetingReserveService;

    @Resource
    private MeetingFeignClient meetingFeignClient;

    @Resource
    private UserFeignClient userFeignClient;



    @PostMapping("add")
    @AuthCheck(mustRole = UserConstant.EMPLOYEE_ROLE)
    public BaseResponse<Long> meetingReserveAdd(@RequestBody MeetingReserveAddRequest meetingReserveAddRequest,HttpServletRequest request){
        if (meetingReserveAddRequest == null || meetingReserveAddRequest.getMeetingId() == null || meetingReserveAddRequest.getMeetingStartTime() == null || meetingReserveAddRequest.getMeetingEndTime() == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long meetingReserveId = meetingReserveService.doMeetingReserve(meetingReserveAddRequest,user);
        return ResultUtils.success(meetingReserveId);
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.EMPLOYEE_ROLE)
    public BaseResponse<Boolean> deleteMeetingReserve(@RequestBody MeetingReserveDeleteRequest meetingReserveDeleteRequest,HttpServletRequest request) {

        if (meetingReserveDeleteRequest == null || meetingReserveDeleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        MeetingReserve meetingReserve = meetingReserveService.getById(meetingReserveDeleteRequest.getId());
        boolean result = meetingReserve.getUserId().equals(user.getId()) || user.getUserRole().equals(UserRoleEnum.ADMIN.getValue());
        ThrowUtils.throwIf(!result,ErrorCode.FORBIDDEN_ERROR,"不是会议发起人或管理员无法取消会议");
        boolean b = meetingReserveService.removeById(meetingReserveDeleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 分页获取会议记录列表
     *
     * @param meetingReserveQueryRequest
     * @param
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<MeetingReserveVO>> listMeetingReserveByPage(@RequestBody MeetingReserveQueryRequest meetingReserveQueryRequest) {
        long current = meetingReserveQueryRequest.getCurrent();
        long size = meetingReserveQueryRequest.getPageSize();
        Page<MeetingReserve> meetingReservePage = meetingReserveService.page(new Page<>(current, size),
                meetingReserveService.getQueryWrapper(meetingReserveQueryRequest));
        Page<MeetingReserveVO> meetingReserveVOPage = meetingReserveService.getMeetingReserveVOPage(meetingReservePage);
        return ResultUtils.success(meetingReserveVOPage);
    }

    /**
     * 分页获取自己的会议记录
     *
     * @param meetingReserveQueryRequest
     * @param
     * @return
     */
    @PostMapping("/list/page/my")
    public BaseResponse<Page<MeetingReserveVO>> listMyMeetingReserveByPage(@RequestBody MeetingReserveQueryRequest meetingReserveQueryRequest,HttpServletRequest request) {
        long current = meetingReserveQueryRequest.getCurrent();
        long size = meetingReserveQueryRequest.getPageSize();
        User user = userFeignClient.getLoginUser(request);

        meetingReserveQueryRequest.setMeetingParticipants(Collections.singletonList(user.getId()));

        Page<MeetingReserve> meetingReservePage = meetingReserveService.page(new Page<>(current, size),
                meetingReserveService.getMyQueryWrapper(meetingReserveQueryRequest));
        Page<MeetingReserveVO> meetingReserveVOPage = meetingReserveService.getMeetingReserveVOPage(meetingReservePage);
        return ResultUtils.success(meetingReserveVOPage);
    }

    /**
     * 更新会议记录
     *
     * @param meetingReserveUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.EMPLOYEE_ROLE)
    public BaseResponse<Boolean> updateMeetingReserve(@RequestBody MeetingReserveUpdateRequest meetingReserveUpdateRequest,HttpServletRequest request) {
        if (meetingReserveUpdateRequest == null || meetingReserveUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        MeetingReserve meetingReserve = meetingReserveService.getById(meetingReserveUpdateRequest.getId());
        boolean result = meetingReserve.getUserId().equals(user.getId()) || user.getUserRole().equals(UserRoleEnum.ADMIN.getValue());
        ThrowUtils.throwIf(!result,ErrorCode.FORBIDDEN_ERROR,"不是会议发起人或管理员无法修改会议记录");

        if(meetingReserveUpdateRequest.getMeetingId()!= null){
            Meeting meeting = new Meeting();
            MeetingQueryRequest meetingQueryRequest = new MeetingQueryRequest();
            meeting.setMeetingStatus(MeetingStatusEnum.READY.getValue());
            meeting.setId(meetingReserveUpdateRequest.getMeetingId());
            boolean a = meetingFeignClient.isExist(meetingQueryRequest);
            ThrowUtils.throwIf(!a,ErrorCode.SYSTEM_ERROR,"会议室维护中或会议室不存在");
        }

        if(meetingReserveUpdateRequest.getMeetingStartTime() != null && meetingReserveUpdateRequest.getMeetingEndTime() != null){
            MeetingReserve meetingReserveTmp = new MeetingReserve();
            MeetingReserveQueryRequest meetingReserveQueryRequest = new MeetingReserveQueryRequest();
            meetingReserveTmp.setMeetingId(meetingReserveUpdateRequest.getMeetingId());
            meetingReserveTmp.setMeetingStartTime(meetingReserveUpdateRequest.getMeetingStartTime());
            meetingReserveTmp.setMeetingEndTime(meetingReserveUpdateRequest.getMeetingEndTime());
            BeanUtils.copyProperties(meetingReserveTmp,meetingReserveQueryRequest);
            Date currentDate = new Date();
            boolean flag = meetingReserveTmp.getMeetingStartTime().after(currentDate) && meetingReserveTmp.getMeetingEndTime().after(meetingReserveTmp.getMeetingStartTime());
            ThrowUtils.throwIf(!flag, ErrorCode.REQUEST_ERROR,"时间非法");
            Long meetingReserveSum = meetingReserveService.count(meetingReserveService.getQueryWrapper(meetingReserveQueryRequest));
            ThrowUtils.throwIf(meetingReserveSum > 0, ErrorCode.REQUEST_ERROR,"该时间段会议室已被占用");
        }

        List<Long> participants = meetingReserveUpdateRequest.getMeetingParticipants();
        if(participants != null){
            meetingReserve.setMeetingParticipants(JSONUtil.toJsonStr(participants));
        }

        boolean b = meetingReserveService.updateById(meetingReserve);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(true);
    }
}

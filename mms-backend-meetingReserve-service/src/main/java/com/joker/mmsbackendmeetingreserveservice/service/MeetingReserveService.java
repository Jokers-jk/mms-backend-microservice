package com.joker.mmsbackendmeetingreserveservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joker.mmsbackendmodel.dto.meeting.MeetingQueryRequest;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveAddRequest;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveQueryRequest;
import com.joker.mmsbackendmodel.entity.Meeting;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.vo.MeetingReserveVO;


/**
 * 会议室预定服务
 */
public interface MeetingReserveService extends IService<MeetingReserve> {



    QueryWrapper<MeetingReserve> getQueryWrapper(MeetingReserveQueryRequest meetingReserveQueryRequest);

    QueryWrapper<MeetingReserve> getMyQueryWrapper(MeetingReserveQueryRequest meetingReserveQueryRequest);


    Page<MeetingReserveVO> getMeetingReserveVOPage(Page<MeetingReserve>  meetingReservePage);

    MeetingReserveVO getMeetingReserveVO(MeetingReserve meetingReserve);

    Long doMeetingReserve(MeetingReserveAddRequest meetingReserveAddRequest, User user);
}

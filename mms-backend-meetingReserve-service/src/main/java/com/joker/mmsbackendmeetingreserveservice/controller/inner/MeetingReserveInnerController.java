package com.joker.mmsbackendmeetingreserveservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendmeetingreserveservice.service.MeetingReserveService;

import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveQueryRequest;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import com.joker.mmsbackendserviceclient.service.MeetingReserveFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class MeetingReserveInnerController implements MeetingReserveFeignClient {

    @Resource
    private MeetingReserveService meetingReserveService;

    @GetMapping("/get/id")
    @Override
    public MeetingReserve getMeetingReserveById(Long meetingReserveId) {
        return meetingReserveService.getById(meetingReserveId);
    }

    @Override
    public boolean updateMeetingReserveById(MeetingReserve meetingReserve) {
        return meetingReserveService.updateById(meetingReserve);
    }

    @Override
    public boolean isExist(MeetingReserveQueryRequest meetingReserveQueryRequest) {
        Long count = meetingReserveService.count(meetingReserveService.getQueryWrapper(meetingReserveQueryRequest));
        if(count > 0){
            return true;
        }
        return false;
    }


}

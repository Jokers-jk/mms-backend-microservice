package com.joker.mmsbackendmeetingservice.controller.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendmeetingservice.service.MeetingService;
import com.joker.mmsbackendmodel.dto.meeting.MeetingQueryRequest;
import com.joker.mmsbackendmodel.entity.Meeting;
import com.joker.mmsbackendmodel.vo.MeetingVO;
import com.joker.mmsbackendserviceclient.service.MeetingFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class MeetingInnerController implements MeetingFeignClient {


    @Resource
    private MeetingService meetingService;

    @Override
    @GetMapping("/get/id")
    public Meeting getById(Long id) {
        return meetingService.getById(id);
    }



    @Override
    @PostMapping("/select")
    public boolean isExist(MeetingQueryRequest meetingQueryRequest){
        Long count = meetingService.count(meetingService.getQueryWrapper(meetingQueryRequest));
        if(count > 0){
            return true;
        }
        return false;
    }
}

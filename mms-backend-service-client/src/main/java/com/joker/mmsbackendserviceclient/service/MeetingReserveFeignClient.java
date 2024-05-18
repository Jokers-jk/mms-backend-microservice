package com.joker.mmsbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmodel.dto.meetingreserve.MeetingReserveQueryRequest;
import com.joker.mmsbackendmodel.entity.MeetingReserve;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "mms-backend-meetingReserve-service", path = "/api/meeting_reserve/inner")
public interface MeetingReserveFeignClient {

    @GetMapping("/get/id")
    MeetingReserve getMeetingReserveById(@RequestParam("meetingReserveId") Long meetingReserveId);

    @PostMapping("/udpate")
    boolean updateMeetingReserveById(@RequestBody MeetingReserve meetingReserve);


    @PostMapping("/select")
    boolean isExist(@RequestBody MeetingReserveQueryRequest meetingReserveQueryRequest);





}

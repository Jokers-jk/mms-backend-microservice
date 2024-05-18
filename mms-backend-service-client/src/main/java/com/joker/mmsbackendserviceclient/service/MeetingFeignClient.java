package com.joker.mmsbackendserviceclient.service;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmodel.dto.meeting.MeetingQueryRequest;
import com.joker.mmsbackendmodel.entity.Meeting;
import com.joker.mmsbackendmodel.vo.MeetingVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mms-backend-meeting-service", path = "/api/meeting/inner")
public interface MeetingFeignClient {

    @GetMapping("/get/id")
    Meeting getById(@RequestParam("id") Long id);

    @PostMapping("/select")
    boolean isExist(@RequestBody MeetingQueryRequest meetingQueryRequest);

    default MeetingVO getMeetingVO(Meeting meeting){
        MeetingVO meetingVO = MeetingVO.objToVo(meeting);
        return meetingVO;
    }



}

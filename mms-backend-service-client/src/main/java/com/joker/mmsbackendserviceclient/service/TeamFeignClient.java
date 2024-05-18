package com.joker.mmsbackendserviceclient.service;


import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.entity.Team;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mms-backend-team-service", path = "/api/team/inner")
public interface TeamFeignClient {

    @PostMapping("/select")
    boolean isExist(@RequestBody TeamQueryRequest teamQueryRequest);

    @GetMapping("/get/id")
    Team getById(@RequestParam("id") Long id);



}

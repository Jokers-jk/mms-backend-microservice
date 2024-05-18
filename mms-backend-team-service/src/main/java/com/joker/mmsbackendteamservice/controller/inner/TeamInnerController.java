package com.joker.mmsbackendteamservice.controller.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.entity.Team;
import com.joker.mmsbackendteamservice.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.joker.mmsbackendserviceclient.service.TeamFeignClient;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class TeamInnerController implements TeamFeignClient{


    @Resource
    private TeamService teamService;

    @Override
    @PostMapping("/select")
    public boolean isExist(TeamQueryRequest teamQueryRequest) {
        Long count = teamService.count(teamService.getQueryWrapper(teamQueryRequest));
       if(count > 0){
           return true;
       }
       return false;
    }

    @Override
    @GetMapping("/get/id")
    public Team getById(Long id) {
        return teamService.getById(id);
    }

}

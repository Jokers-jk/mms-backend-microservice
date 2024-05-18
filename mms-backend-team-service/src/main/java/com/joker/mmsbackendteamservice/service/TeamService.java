package com.joker.mmsbackendteamservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.dto.user.UserQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendmodel.entity.Team;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.vo.TeamVO;


/**
 * 团队服务
 */
public interface TeamService extends IService<Team> {

    QueryWrapper<Team> getQueryWrapper(TeamQueryRequest teamQueryRequest);

    Page<TeamVO> getTeamVOPage(Page<Team> teamPage);

    TeamVO getTeamVO(Team team);


}

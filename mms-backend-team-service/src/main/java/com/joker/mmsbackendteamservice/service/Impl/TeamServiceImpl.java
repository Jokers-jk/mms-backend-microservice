package com.joker.mmsbackendteamservice.service.Impl;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.dto.user.UserQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendmodel.entity.Team;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.vo.DepartmentVO;
import com.joker.mmsbackendmodel.vo.TeamVO;
import com.joker.mmsbackendserviceclient.service.DepartmentFeignClient;
import com.joker.mmsbackendteamservice.mapper.TeamMapper;
import com.joker.mmsbackendteamservice.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Resource
    private DepartmentFeignClient departmentFeignClient;

    @Override
    public QueryWrapper<Team> getQueryWrapper(TeamQueryRequest teamQueryRequest) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if(teamQueryRequest == null){
            return queryWrapper;
        }
        Long teamId = teamQueryRequest.getId();
        String teamName = teamQueryRequest.getTeamName();
        Long departmentId = teamQueryRequest.getDepartmentId();
        String sortField = teamQueryRequest.getSortField();
        String sortOrder = teamQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(teamId), "id",teamId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(departmentId), "departmentId",departmentId);
        queryWrapper.eq(StringUtils.isNotBlank(teamName), "teamName", teamName);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public TeamVO getTeamVO(Team team) {
        TeamVO teamVO = new TeamVO();
        BeanUtils.copyProperties(team,teamVO);
        Department department = departmentFeignClient.getById(team.getDepartmentId());
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department,departmentVO);
        teamVO.setDepartmentVO(departmentVO);
        return teamVO;
    }


    @Override
    public Page<TeamVO> getTeamVOPage(Page<Team> teamPage) {
        List<Team> teamList = teamPage.getRecords();
        Page<TeamVO> teamVOPage = new Page<>(teamPage.getCurrent(), teamPage.getSize(), teamPage.getTotal());
        if (CollUtil.isEmpty(teamList)) {
            return  teamVOPage;
        }
        List<TeamVO> teamVOList = teamList.stream().map(team ->{
            return getTeamVO(team);
        }).collect(Collectors.toList());
        teamVOPage.setRecords(teamVOList);
        return teamVOPage;
    }


}

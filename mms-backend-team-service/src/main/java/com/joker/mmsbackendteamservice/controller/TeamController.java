package com.joker.mmsbackendteamservice.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joker.mmsbackendcommon.annotation.AuthCheck;
import com.joker.mmsbackendcommon.common.BaseResponse;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.common.ResultUtils;
import com.joker.mmsbackendcommon.constant.UserConstant;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.exception.ThrowUtils;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.dto.team.*;
import com.joker.mmsbackendmodel.vo.TeamVO;
import com.joker.mmsbackendmodel.dto.user.UserQueryRequest;
import com.joker.mmsbackendmodel.entity.Team;
import com.joker.mmsbackendserviceclient.service.DepartmentFeignClient;
import com.joker.mmsbackendserviceclient.service.UserFeignClient;
import com.joker.mmsbackendteamservice.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private DepartmentFeignClient departmentFeignClient;

    @Resource
    private UserFeignClient userFeignClient;


    @Resource
    private ObjectMapper objectMapper;





    @PostMapping("add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> teamAdd(@RequestBody TeamAddRequest teamAddRequest){
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        Team team = new Team();
        TeamQueryRequest teamQueryRequest = new TeamQueryRequest();
        BeanUtils.copyProperties(teamAddRequest,team);
        BeanUtils.copyProperties(team,teamQueryRequest);
        DepartmentQueryRequest departmentQueryRequest = new DepartmentQueryRequest();
        departmentQueryRequest.setId(teamAddRequest.getDepartmentId());
        boolean a = departmentFeignClient.isExist(departmentQueryRequest);
        ThrowUtils.throwIf(!a, ErrorCode.REQUEST_ERROR,"部门不存在");
        Long result = teamService.count(teamService.getQueryWrapper(teamQueryRequest));
        ThrowUtils.throwIf(result > 0, ErrorCode.REQUEST_ERROR,"团队已存在");

        boolean b = teamService.save(team);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(team.getId());
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest) {
        if (teamDeleteRequest == null || teamDeleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setTeamId(teamDeleteRequest.getId());
        boolean a = userFeignClient.isExist(userQueryRequest);
        ThrowUtils.throwIf(!a, ErrorCode.REQUEST_ERROR,"团队下存在用户");
        boolean b = teamService.removeById(teamDeleteRequest.getId());
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(b);
    }

    /**
     * 分页获取团队列表
     *
     * @param teamQueryRequest
     * @param
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<TeamVO>> listTeamByPage(@RequestBody TeamQueryRequest teamQueryRequest) {
        long current = teamQueryRequest.getCurrent();
        long size = teamQueryRequest.getPageSize();

        Page<Team> teamPage = teamService.page(new Page<>(current, size),
                teamService.getQueryWrapper(teamQueryRequest));
        Page<TeamVO> teamVOPage = teamService.getTeamVOPage(teamPage);
        return ResultUtils.success(teamVOPage);
    }

    /**
     * 更新团队
     *
     * @param teamUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest) {
        if (teamUpdateRequest == null || teamUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        Team team = new Team();
        TeamQueryRequest teamQueryRequest = new TeamQueryRequest();
        BeanUtils.copyProperties(teamUpdateRequest, team);
        BeanUtils.copyProperties(team,teamQueryRequest);
        if(teamUpdateRequest.getDepartmentId() != null ){
            DepartmentQueryRequest departmentQueryRequest = new DepartmentQueryRequest();
            departmentQueryRequest.setId(teamUpdateRequest.getDepartmentId());
            boolean a = departmentFeignClient.isExist(departmentQueryRequest);
            ThrowUtils.throwIf(!a, ErrorCode.REQUEST_ERROR,"部门不存在");
        }
        Long result = teamService.count(teamService.getQueryWrapper(teamQueryRequest));
        ThrowUtils.throwIf(result > 0, ErrorCode.REQUEST_ERROR,"团队已存在");
        boolean b = teamService.updateById(team);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(true);
    }
}

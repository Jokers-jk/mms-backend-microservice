package com.joker.mmsbackenddepartmentservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.joker.mmsbackendcommon.annotation.AuthCheck;
import com.joker.mmsbackendcommon.common.BaseResponse;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.common.ResultUtils;
import com.joker.mmsbackendcommon.constant.UserConstant;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.exception.ThrowUtils;
import com.joker.mmsbackenddepartmentservice.service.DepartmentService;
import com.joker.mmsbackendmodel.dto.department.*;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendserviceclient.service.TeamFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private TeamFeignClient teamFeignClient;



    @PostMapping("add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> departmentAdd(@RequestBody DepartmentAddRequest departmentAddRequest){
        if (departmentAddRequest == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        Department department = new Department();
        DepartmentQueryRequest departmentQueryRequest = new DepartmentQueryRequest();
        BeanUtils.copyProperties(departmentAddRequest,department);
        BeanUtils.copyProperties(department,departmentQueryRequest);
        Long result = departmentService.count(departmentService.getQueryWrapper(departmentQueryRequest));
        ThrowUtils.throwIf(result > 0, ErrorCode.REQUEST_ERROR,"部门已存在");
        boolean b = departmentService.save(department);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(department.getId());
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteDepartment(@RequestBody DepartmentDeleteRequest departmentDeleteRequest) {
        if (departmentDeleteRequest == null || departmentDeleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        TeamQueryRequest teamQueryRequest = new TeamQueryRequest();
        teamQueryRequest.setDepartmentId(departmentDeleteRequest.getId());
        boolean a = teamFeignClient.isExist(teamQueryRequest);
        ThrowUtils.throwIf(!a, ErrorCode.REQUEST_ERROR,"该部门下存在团队");
        boolean b = departmentService.removeById(departmentDeleteRequest.getId());
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(b);
    }

    /**
     * 分页获取部门列表
     *
     * @param departmentQueryRequest
     * @param
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Department>> listDepartmentByPage(@RequestBody DepartmentQueryRequest departmentQueryRequest) {
        long current = departmentQueryRequest.getCurrent();
        long size = departmentQueryRequest.getPageSize();
        Page<Department> departmentPage = departmentService.page(new Page<>(current, size),
                departmentService.getQueryWrapper(departmentQueryRequest));
        return ResultUtils.success(departmentPage);
    }

    /**
     * 更新部门
     *
     * @param departmentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateDepartment(@RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        if (departmentUpdateRequest == null || departmentUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.REQUEST_ERROR);
        }
        Department department = new Department();
        DepartmentQueryRequest departmentQueryRequest = new DepartmentQueryRequest();
        BeanUtils.copyProperties(departmentUpdateRequest, department);
        BeanUtils.copyProperties(department, departmentQueryRequest);
        Long result = departmentService.count(departmentService.getQueryWrapper(departmentQueryRequest));
        ThrowUtils.throwIf(result > 0, ErrorCode.REQUEST_ERROR,"部门已存在");
        boolean b = departmentService.updateById(department);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR);
        return ResultUtils.success(b);
    }


}

package com.joker.mmsbackenddepartmentservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendmodel.entity.Team;


/**
 * 部门服务
 */
public interface DepartmentService extends IService<Department> {


     QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest);


}

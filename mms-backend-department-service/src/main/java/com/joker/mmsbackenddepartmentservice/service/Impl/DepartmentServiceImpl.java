package com.joker.mmsbackenddepartmentservice.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackenddepartmentservice.mapper.DepartmentMapper;
import com.joker.mmsbackenddepartmentservice.service.DepartmentService;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.dto.team.TeamQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendmodel.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 部门服务实现
 *
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public QueryWrapper<Department> getQueryWrapper(DepartmentQueryRequest departmentQueryRequest) {
     QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
     if(departmentQueryRequest == null){
        return queryWrapper;
     }
     Long departmentId = departmentQueryRequest.getId();
     String departmentName = departmentQueryRequest.getDepartmentName();
     String sortField = departmentQueryRequest.getSortField();
     String sortOrder = departmentQueryRequest.getSortOrder();

     queryWrapper.eq(ObjectUtils.isNotEmpty(departmentId), "id",departmentId);
     queryWrapper.eq(StringUtils.isNotBlank(departmentName), "departmentName", departmentName);
     queryWrapper.eq("isDelete",false);
     queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
     return queryWrapper;
    }

}

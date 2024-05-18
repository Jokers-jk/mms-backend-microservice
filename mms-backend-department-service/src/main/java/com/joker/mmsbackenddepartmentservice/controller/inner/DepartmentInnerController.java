package com.joker.mmsbackenddepartmentservice.controller.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.common.BaseResponse;
import com.joker.mmsbackendcommon.common.ResultUtils;
import com.joker.mmsbackenddepartmentservice.service.DepartmentService;
import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendserviceclient.service.DepartmentFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class DepartmentInnerController implements DepartmentFeignClient {

    @Resource
    private DepartmentService departmentService;

    @Override
    @PostMapping("/select")
    public boolean isExist(DepartmentQueryRequest departmentQueryRequest) {
        Long count = departmentService.count(departmentService.getQueryWrapper(departmentQueryRequest));
        if(count > 0){
            return true;
        }
        return false;
    }

    @Override
    @GetMapping("/get/id")
    public Department getById(Long id) {
        return departmentService.getById(id);
    }


}

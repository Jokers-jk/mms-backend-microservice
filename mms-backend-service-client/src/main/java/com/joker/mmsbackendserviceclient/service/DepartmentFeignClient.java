package com.joker.mmsbackendserviceclient.service;


import com.joker.mmsbackendmodel.dto.department.DepartmentQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mms-backend-department-service", path = "/api/department/inner")
public interface DepartmentFeignClient {

    @PostMapping("/select")
    boolean isExist(@RequestBody DepartmentQueryRequest departmentQueryRequest);

    @GetMapping("/get/id")
    Department getById(@RequestParam("id") Long id);


}

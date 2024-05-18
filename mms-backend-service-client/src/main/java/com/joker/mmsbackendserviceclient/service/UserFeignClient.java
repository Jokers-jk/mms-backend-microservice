package com.joker.mmsbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.constant.CommonConstant;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendcommon.utils.SqlUtils;
import com.joker.mmsbackendmodel.dto.user.UserQueryRequest;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.joker.mmsbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

@FeignClient(name = "mms-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {


    @PostMapping("/select")
    boolean isExist(@RequestBody UserQueryRequest userQueryRequest);

    @GetMapping("/get/vo/id/")
    UserVO getUserVO(@RequestParam("id") Long id);


     default User getLoginUser(HttpServletRequest request){
         // 先判断是否已登录
         Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
         User currentUser = (User) userObj;
         if (currentUser == null || currentUser.getId() == null) {
             throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
         }
         return currentUser;
     }

     @GetMapping("/get/id")
     User getById(@RequestParam("id") Long id);


}

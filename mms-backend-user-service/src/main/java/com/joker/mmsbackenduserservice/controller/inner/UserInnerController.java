package com.joker.mmsbackenduserservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joker.mmsbackendcommon.common.ErrorCode;
import com.joker.mmsbackendcommon.exception.BusinessException;
import com.joker.mmsbackendmodel.dto.user.UserQueryRequest;
import com.joker.mmsbackendmodel.entity.Department;
import com.joker.mmsbackendmodel.entity.Team;
import com.joker.mmsbackendmodel.entity.User;
import com.joker.mmsbackendmodel.vo.DepartmentVO;
import com.joker.mmsbackendmodel.vo.TeamVO;
import com.joker.mmsbackendmodel.vo.UserVO;
import com.joker.mmsbackendserviceclient.service.DepartmentFeignClient;
import com.joker.mmsbackendserviceclient.service.TeamFeignClient;
import com.joker.mmsbackendserviceclient.service.UserFeignClient;
import com.joker.mmsbackenduserservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.joker.mmsbackendcommon.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {


    @Resource
    private UserService userService;

    @Resource
    private TeamFeignClient teamFeignClient;


    @Resource
    private DepartmentFeignClient departmentFeignClient;


    @Override
    @PostMapping("/select")
    public boolean isExist(UserQueryRequest userQueryRequest) {
        Long count = userService.count(userService.getQueryWrapper(userQueryRequest));
        if(count > 0){
            return true;
        }
        return false;
    }

    @Override
    @GetMapping("/get/vo/id")
    public UserVO getUserVO(Long id) {
        if (id == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        User user = userService.getById(id);
        BeanUtils.copyProperties(user, userVO);
        Team team = teamFeignClient.getById(user.getTeamId());
        TeamVO teamVO = new TeamVO();
        BeanUtils.copyProperties(team,teamVO);
        Department department = departmentFeignClient.getById(team.getDepartmentId());
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department,departmentVO);
        teamVO.setDepartmentVO(departmentVO);
        userVO.setTeamVO(teamVO);
        return userVO;
    }



    @Override
    @GetMapping("/get/id")
    public User getById(Long id) {
        return userService.getById(id);
    }

}

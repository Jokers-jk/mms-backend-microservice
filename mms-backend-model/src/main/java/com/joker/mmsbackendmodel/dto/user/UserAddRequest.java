package com.joker.mmsbackendmodel.dto.user;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户添加请求
 *
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户电话
     */
    private Long userPhoneNumber;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;


    private static final long serialVersionUID = 1L;
}

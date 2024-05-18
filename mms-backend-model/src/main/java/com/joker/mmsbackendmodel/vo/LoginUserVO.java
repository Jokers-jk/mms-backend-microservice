package com.joker.mmsbackendmodel.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class LoginUserVO implements Serializable {

    /**
     * id
     */
    private Long id;

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


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 团队信息
     */
    private TeamVO teamVO;

    private static final long serialVersionUID = 1L;

}

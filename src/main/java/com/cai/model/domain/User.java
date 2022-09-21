package com.cai.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName userinfo
 */
@TableName(value ="userinfo")
@Data

public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 登录账号
     */
    private String loginId;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别 0-保密 1-男 2-女
     */
    private Integer gender;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 用户状态 0-用户状态正常 1-用户状态异常
     */
    private Integer userStatus;

    /**
     * 用户权限 0-普通用户 1-管理员
     */
    private Integer isManager;

    /**
     * 用户注册时间
     */
    private Date createTime;

    /**
     * 用户修改信息时间
     */
    private Date updateTime;

    /**
     * 是否删除 0-正常
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
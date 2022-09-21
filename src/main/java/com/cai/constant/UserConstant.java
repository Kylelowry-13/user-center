package com.cai.constant;

/**
 * @author kyle
 * @description: 用户全局常量
 * @date 2022/9/14 0:56
 */
public interface UserConstant {
    /**
     * 用户登录键
     */
    String USER_LOGIN_STATE = "userLoginState";

    //--------用户权限
    /**
     * 普通用户
     */
    int DEFAULT_USER = 0;
    /**
     * 管理员
     */
    int MANAGER_USER = 1;
}

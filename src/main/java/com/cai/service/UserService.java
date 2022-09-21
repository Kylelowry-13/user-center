package com.cai.service;

import com.cai.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 1
* @description 针对表【userinfo】的数据库操作Service
* @createDate 2022-09-07 23:54:58
*/
public interface UserService extends IService<User> {



    long userRegister(String userName, String loginId, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param loginId    登陆账号
     * @param userPassword     密码
     * @param request
     * @return    脱敏后的用户信息
     */
    User login(String loginId, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);
}

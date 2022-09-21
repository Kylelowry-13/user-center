package com.cai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cai.model.domain.User;
import com.cai.service.UserService;
import com.cai.mapper.UserMapper;
import com.cai.utils.CheckCharacter;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.cai.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 1
* @description 针对表【userinfo】的数据库操作Service实现
* @createDate 2022-09-07 23:54:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {



    @Resource
    UserMapper userMapper;


    /**
     * 密码加密盐值
     */
    public static final String SALT = "kyle";




    @Override
    public long userRegister(String userName, String loginId, String userPassword, String checkPassword) {
        //校验昵称是否是2-8位
        if (userName.length() < 2 || userName.length() > 8){
            return -1;
        }
        //校验密码是否在6-20位
        if (userPassword.length() <6 || userPassword.length() > 20){
            return -1;
        }
//        //校验登陆账号是否在6-20位
//        if (user.getLoginId().length() <6 || user.getLoginId().length() > 20){
//            return -1;
//        }
//        //校验邮箱格式是否正确
//        if (!CheckCharacter.checkEmail(user.getEmail())){
//            return -1;
//        }
//        //校验手机号是否为11位
//        if (user.getPhoneNumber().length() != 11){
//            return -1;
//        }
        //账号不能包含特殊符号
        if (CheckCharacter.checkLoginId(loginId)){
            return -1;
        }

        //昵称不能包含特殊符号
        if (CheckCharacter.checkLoginId(userName)){
            return -1;
        }

        //验证两次密码是否一致
        if (!userPassword.equals(checkPassword)){
            return -1;
        }
        //昵称不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        int count = this.count(queryWrapper);
        if (count > 0 ){
            return -1;
        }
        //登录账号不能重复
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("login_id", loginId);
        int count2 = this.count(queryWrapper2);
        if (count2 > 0 ){
            return -1;
        }


        User user = new User();
        user.setUserName(userName);
        user.setLoginId(loginId);
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encryptPassword);

        //将数据添加到数据库
        boolean save = this.save(user);
        if (!save){
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param loginId   账号
     * @param userPassword  密码
     * @return  用户信息
     */
    @Override
    public User login(String loginId, String userPassword, HttpServletRequest request) {
        //校验账号密码是否为空
        if (loginId == null || userPassword == null) {
            return null;
        }
        //校验登陆账号是否在6-20位
        if (loginId.length() <6 || loginId.length() > 20){
            return null;
        }
        //校验密码是否在6-20位
        if (userPassword.length() <6 || userPassword.length() > 20){
            return null;
        }
        //账号不能包含特殊符号
        if (CheckCharacter.checkLoginId(loginId)){
            return null;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //校验账号密码是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_id",loginId);
        queryWrapper.eq("user_password",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return null;
        }
        //将用户信息脱敏
        User safetyUser = getSafetyUser(user);


        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 数据脱敏
     * @param originUser
     * @return 脱敏后的数据
     */
    @Override
    public User getSafetyUser(User originUser){
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setLoginId(originUser.getLoginId());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPhoneNumber(originUser.getPhoneNumber());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setIsManager(originUser.getIsManager());

        return safetyUser;
    }


}





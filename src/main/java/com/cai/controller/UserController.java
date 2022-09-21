package com.cai.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cai.model.domain.User;
import com.cai.model.domain.request.UserLoginRequest;
import com.cai.model.domain.request.UserRegisterRequest;
import com.cai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cai.constant.UserConstant.MANAGER_USER;
import static com.cai.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author kyle
 * @description: 用户信息控制层
 * @date 2022/9/10 11:19
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 注册接口
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public long register(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return -1;
        }
//        User user = userRegisterRequest.getUser();
//        String loginId = userRegisterRequest.getUser().getLoginId();
//        String userPassword = userRegisterRequest.getUser().getUserPassword();
        String userName = userRegisterRequest.getUserName();
        String loginId = userRegisterRequest.getLoginId();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userName,loginId,userPassword,checkPassword)){
            return -2;
        }
        long l = userService.userRegister(userName,loginId,userPassword,checkPassword);
        return l;
    }

    /**
     * 登录接口
     * @param loginInfo
     * @param request
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest loginInfo, HttpServletRequest request){
        if (loginInfo == null){
            return null;
        }
        String loginId = loginInfo.getLoginId();
        String userPassword = loginInfo.getUserPassword();
        if (StringUtils.isAnyBlank(loginId,userPassword)){
            return null;
        }
        User userInfo = userService.login(loginId, userPassword, request);
        return userInfo;
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        //TODO 校验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    /**
     * 模糊查询接口
     * @param userName
     * @return
     */
    @GetMapping("/search")
    public List<User> search(String userName,HttpServletRequest request){
        if (!isManager(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.like("user_name",userName);
        }

        //return userService.list(queryWrapper);
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public Boolean delete(@RequestBody long id,HttpServletRequest request){
        if (!isManager(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    private boolean isManager(HttpServletRequest request){
        Object isManager = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) isManager;
        // 判断是否为管理员，仅管理员可查
        return user != null && user.getIsManager() == MANAGER_USER;
    }
}

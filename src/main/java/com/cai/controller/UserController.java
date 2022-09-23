package com.cai.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.cai.common.BaseResponse;
import com.cai.common.ErrorCode;
import com.cai.common.ResultUtils;
import com.cai.exception.BusinessException;
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
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userName = userRegisterRequest.getUserName();
        String loginId = userRegisterRequest.getLoginId();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userName,loginId,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userName,loginId,userPassword,checkPassword);

        return ResultUtils.success(result);
    }

    /**
     * 登录接口
     * @param loginInfo
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest loginInfo, HttpServletRequest request){
        if (loginInfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误的账号或密码");
        }
        String loginId = loginInfo.getLoginId();
        String userPassword = loginInfo.getUserPassword();
        if (StringUtils.isAnyBlank(loginId,userPassword)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误的账号或密码");
        }
        User userInfo = userService.login(loginId, userPassword, request);
        return ResultUtils.success(userInfo);
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        //TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        BaseResponse<User> success = ResultUtils.success(safetyUser);
        return success;
    }

    /**
     * 模糊查询接口
     * @param userName
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> search(String userName,HttpServletRequest request){
        if (!isManager(request)){
           // return new ArrayList<>();
//            return null;
            throw new BusinessException(ErrorCode.NOT_AUTH,"您还不是管理员");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.like("user_name",userName);
        }

        //return userService.list(queryWrapper);
        List<User> userList = userService.list(queryWrapper);
        List<User> collect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    /**
     * 根据id删除用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody long id,HttpServletRequest request){
        if (!isManager(request)){
//            return null;
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (id <= 0){
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    private Boolean isManager(HttpServletRequest request){
        Object isManager = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) isManager;
        // 判断是否为管理员，仅管理员可查
        return user != null && user.getIsManager() == MANAGER_USER;
    }

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request){
        if (request == null) {
//            return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userLogout(request);
    }
}

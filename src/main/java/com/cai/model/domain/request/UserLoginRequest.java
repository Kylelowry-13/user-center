package com.cai.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kyle
 * @description: 用户登录请求
 * @date 2022/9/11 1:26
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -9007300327438533333L;
    private String loginId;
    private String userPassword;
}

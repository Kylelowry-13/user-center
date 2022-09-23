package com.cai.model.domain.request;

import com.cai.model.domain.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kyle
 * @description: 以JSON格式接受前端请求数据
 * @date 2022/9/10 11:32
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7286964142422671929L;

    private String userName;
    private String loginId;
    private String userPassword;
    private String checkPassword;
}

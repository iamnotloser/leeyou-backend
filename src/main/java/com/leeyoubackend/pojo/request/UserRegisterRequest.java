package com.leeyoubackend.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 345678355531357878L;
    private String userAccount;
    private String password;
    private String confirmPassword;
    private String planetCode;
}

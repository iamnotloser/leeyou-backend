package com.leeyoubackend.pojo.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 345678355531357878L;
    private String userAccount;
    private String password;
}

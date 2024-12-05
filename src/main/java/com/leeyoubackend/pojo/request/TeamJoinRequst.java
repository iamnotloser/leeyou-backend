package com.leeyoubackend.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamJoinRequst implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long teamId;



    /**
     * 队伍密码
     */
    private String password;





}

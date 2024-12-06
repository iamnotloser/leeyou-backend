package com.leeyoubackend.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出请求体
 */
@Data
public class TeamQuitRequest implements Serializable {
    private static final long serialVersionUID = -2222222222222222222L;
    /**
     * 队伍id
     */
    private Long teamId;
}

package com.leeyoubackend.pojo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {
    /**
     * 页面大小
     */
    private static final long serialVersionUID = -55544322677L;
    private int pageSize=10;

    /**
     * 当前页码
     */
    private int pageNum=1;
}

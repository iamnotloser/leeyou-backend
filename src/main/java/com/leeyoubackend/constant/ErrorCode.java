package com.leeyoubackend.constant;

/**
 * 错误码
 */

public enum ErrorCode {
    SUCCESS(0, "ok",""),
    PARAMS_ERROR(40000, "请求参数错误",""),
    NULL_ERROR(40001, "请求数据为空",""),
    NOT_LOGIN(40100, "未登录",""),
    NO_AUTH(40101, "无权限",""),
    SYSTEM_ERROR(50000, "系统内部异常",""),
    OPERATION_ERROR(50001,"系统操作异常" ,"" );


    private final String message;
    private final int code;
    private final String description;
    ErrorCode(int code, String message, String description) {
        this.message = message;
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }
}

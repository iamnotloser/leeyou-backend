package com.leeyoubackend.exception;

import com.leeyoubackend.constant.ErrorCode;
import lombok.Data;

/**
 * 自定义异常类
 */
@Data
public class BusinesException extends RuntimeException{
    private final int code;
    private final String description;


    public BusinesException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinesException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();

        this.description = errorCode.getDescription();

    }

    public BusinesException(ErrorCode errorCode, int code, String description) {
        super(errorCode.getMessage());
        this.code = code;
        this.description = description;

    }
    public BusinesException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();

        this.description = description;

    }
}

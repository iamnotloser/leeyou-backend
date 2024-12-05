package com.leeyoubackend.constant;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private  String description;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = null;
    }
    public BaseResponse(int code, String message,String description, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMessage());
    }
    public BaseResponse(ErrorCode errorCode,String message ,String description){
        this(errorCode.getCode(),errorCode.getMessage());
        this.message = message;
        this.data = null;
    }
    public BaseResponse(ErrorCode errorCode ,String description){
        this(errorCode.getCode(),errorCode.getMessage());
        this.description = description;
        this.data = null;
    }

    public static<T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,"success",data);
    }
    public static<T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }


}

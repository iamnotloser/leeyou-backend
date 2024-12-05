package com.leeyoubackend.exception;

import com.leeyoubackend.constant.BaseResponse;
import com.leeyoubackend.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinesException.class)
    public BaseResponse globalExceptionHandler(BusinesException e) {
        log.error("businessexception:"+e.getMessage(),e);
        return new BaseResponse<>(e.getCode(),e.getMessage(),e.getDescription());

    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException:"+e);
        return new BaseResponse<>(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");

    }
}

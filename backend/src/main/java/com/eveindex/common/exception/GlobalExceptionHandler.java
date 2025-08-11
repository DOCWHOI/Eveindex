package com.eveindex.common.exception;

import com.eveindex.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<String> handleException(Exception e) {
        log.error("系统异常", e);
        return BaseResponse.error("系统异常，请联系管理员");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<String> handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return BaseResponse.error(e.getMessage());
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常", e);
        return BaseResponse.error("参数错误：" + e.getMessage());
    }
} 
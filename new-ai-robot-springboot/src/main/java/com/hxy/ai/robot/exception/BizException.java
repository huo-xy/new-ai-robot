package com.hxy.ai.robot.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 10:29
 * @Modified By;
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}

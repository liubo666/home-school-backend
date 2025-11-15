package com.school.cooperation.common.exception;

import lombok.Getter;

/**
 * 业务异常类
 *
 * @author Home School Team
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(String message, Throwable cause) {
        this(500, message, cause);
    }
}
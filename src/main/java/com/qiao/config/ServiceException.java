package com.qiao.config;

import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException {
    private int code;
    private String message;

    public ServiceException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

}

package com.qiao.config;


public enum AppHttpCodeEnum {
    /**
     * 自定义http状态码：最后一个注意用分号
     */
    SUCCESS(200, "操作成功"),
    FAILED(201, "操作失败"),
    AUTHORITY_NOT(503, "权限不足");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}


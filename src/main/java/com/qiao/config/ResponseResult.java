package com.qiao.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
// 实体类与json互转的时候 属性值为null的不参与序列化
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    //http状态码
    private Integer code;
    //反馈信息
    private String msg;
    //响应数据
    private T data;

    //构建ResponseResult构造函数
    //  ResponseResult () 无参形式，返回成功code,msg,
    public ResponseResult() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    //  ResponseResult (data) success的code,msg,和data
    public ResponseResult(T data) {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
        this.data = data;
    }

    //  ResponseResult (code,data) 自定义的code和data
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    //  ResponseResult (code,msg) 自定义code,msg,
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    //ResponseResult (code,msg,data)自定义code,msg,data
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构造静态函数
     */

    //使用范例ResponseResult.okResult()         无参数返回SUCCESS的code，msg
    public static ResponseResult okResult() {
        return new ResponseResult();
    }

    //使用范例ResponseResult.okResult(200, "登录成功")         返回自定义的code和msg
    public static ResponseResult okResult(int code, String msg) {
        return new ResponseResult(code, msg);
    }

    //使用范例ResponseResult.okResult(200, "登录成功", data)   返回自定义的code和msg、数据data
    public static ResponseResult okResult(int code, String msg, Object data) {
        return new ResponseResult(code, msg, data);
    }

    //使用范例ResponseResult.okResult(200, data)              返回自定义的code、数据data
    public static ResponseResult okResult(int code, Object data) {
        return new ResponseResult(code, data);
    }

    //使用范例ResponseResult.okResult(data)                   返回SUCCESS的code,msg和自定义的数据data
    public static ResponseResult okResult(Object data) {
        return new ResponseResult(data);
    }

    //使用范例ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS);   调用okResult返回enums的code和msg
    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMsg());
    }

    //使用范例ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS);   调用okResult返回enums的code和msg和自定义的data
    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, Object data) {
        return okResult(enums.getCode(), enums.getMsg(), data);
    }

}

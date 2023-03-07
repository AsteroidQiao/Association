package com.qiao.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author AsteroidQiao
 * @Create 2023-03-07
 */
@ControllerAdvice
/**
 * 返回异常结果给前端
 * 返回自定义异常
 */
public class ResultErrorException {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseResult ResultError (ServiceException e){
       return ResponseResult.okResult(e.getCode(),e.getMessage());
    }
}

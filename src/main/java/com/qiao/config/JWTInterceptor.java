package com.qiao.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Users;
import com.qiao.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author AsteroidQiao
 * @Create 2023-03-05
 */
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    private UsersService usersService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //添加这行代码，让OPTIONS请求通过
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader("token");
        // 如果token为空，禁止访问
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(401, "无token，请重新登录");
        }
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取 token 中的 uaccount
        String uaccount;
        try {
            uaccount = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(HttpServletResponse.SC_UNAUTHORIZED, "token验证失败，请重新登录");
        }
        // 根据token中的用户名 查询数据库是否存在该用户
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount);
        Users one = usersService.getOne(usersQueryWrapper);
        if (one == null) {
            throw new ServiceException(HttpServletResponse.SC_NOT_FOUND, "用户不存在，请重新登录");
        }
        // sign验证（用户密码） token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(one.getUpwd())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(HttpServletResponse.SC_UNAUTHORIZED, "token验证失败，请重新登录");
        }
        return true;
    }
}

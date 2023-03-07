package com.qiao.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @Author AsteroidQiao
 * @Create 2023-03-05
 */
public class JWTUtils {
    /**
     * 设置token
     * @param name
     * @param sign
     * @return
     */
    public static String setToken(String name, String sign) {
        return JWT.create().withAudience(name).//用户名设置
                withExpiresAt(DateUtil.offsetHour(new Date(), 2)).
                sign(Algorithm.HMAC256(sign));//用 用户密码加签
    }
    /**
     * 解析token
     * 验证token是否合法
     */
    public static DecodedJWT decode(String token, String sign) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }
}

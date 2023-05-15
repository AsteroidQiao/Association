package com.qiao.config;

import java.lang.annotation.*;

/**
 * @Author AsteroidQiao
 * @Create 2023-03-13
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Authorization {
    String value() default "Authority";
}

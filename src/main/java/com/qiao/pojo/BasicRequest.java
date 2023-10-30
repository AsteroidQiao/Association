package com.qiao.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author AsteroidQiao
 * @Create 2023-10-12
 */
@Data
public class BasicRequest implements Serializable {
    private String url;

    private static final long SerialVersionUID = 1L;
}

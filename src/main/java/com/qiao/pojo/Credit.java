package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName credit
 */
@TableName(value ="credit")
@Data
public class Credit implements Serializable {
    /**
     * 学分ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学分数值
     */
    private Integer num;

    /**
     * 学分数量
     */
    private Integer count;

    /**
     * 已经发放数量
     */
    private Integer sent;

    /**
     * 外键 绑定活动
     */
    private String activity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
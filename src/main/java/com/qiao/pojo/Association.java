package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName association
 */
@TableName(value ="association")
@Data
public class Association implements Serializable {
    /**
     * 社团ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 社团名称
     */
    private String name;

    /**
     * 社团简介
     */
    private String info;

    /**
     * 社团图标
     */
    private String icon;

    /**
     * 外键 社团绑定学院
     */
    private String college;

    /**
     * 社团公告
     */
    private String notice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
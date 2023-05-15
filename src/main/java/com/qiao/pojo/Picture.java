package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName picture
 */
@TableName(value ="picture")
@Data
public class Picture implements Serializable {
    /**
     * 活动图片id
     */
    @TableId(type = IdType.AUTO)
    private Integer pid;

    /**
     * 活动图片url
     */
    private String url;

    /**
     * 外键 活动名称
     */
    private String name;

    /**
     * 跳转路由
     */
    private String route;

    /**
     * 逻辑删除
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
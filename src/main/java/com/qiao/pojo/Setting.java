package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName setting
 */
@TableName(value ="setting")
@Data
public class Setting implements Serializable {
    /**
     * ID 方便查询
     */
    @TableId
    private Integer id;

    /**
     * 前台标题
     */
    private String title1;

    /**
     * 后台标题
     */
    private String title2;

    /**
     * 图标
     */
    private String icon;

    /**
     * 前台 标题字体
     */
    private String font1;

    /**
     * 前台 标题颜色
     */
    private String color1;

    /**
     * 后台 标题字体
     */
    private String font2;

    /**
     * 后台 标题颜色
     */
    private String color2;

    /**
     * 页面布局
     */
    private Integer span;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
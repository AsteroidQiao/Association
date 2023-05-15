package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName activity
 */
@TableName(value ="activity")
@Data
public class Activity implements Serializable {
    /**
     * 活动ID
     */
    @TableId(type = IdType.AUTO)
    private Integer acid;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动简介
     */
    private String info;

    /**
     * 参与须知
     */
    private String notice;

    /**
     * 报名开始时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date enrolltime;

    /**
     * 报名结束时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date enrollendtime;

    /**
     * 活动开始时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date starttime;

    /**
     * 活动结束时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endtime;

    /**
     * 外键 主办社团名称
     */
    private String association;

    /**
     * 活动是否通过
     */
    private Integer pass;

    /**
     * 不通过原因，通过则不写
     */
    private String reason;

    /**
     * 逻辑删除
     */
    private Integer isdelete;

    /**
     * 活动显示图片（一张）
     */
    private String url;

    /**
     * 活动路由
     */
    private String route;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
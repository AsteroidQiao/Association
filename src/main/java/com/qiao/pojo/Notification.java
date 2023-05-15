package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName notification
 */
@TableName(value ="notification")
@Data
public class Notification implements Serializable {
    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String msg;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 是否已读
     */
    private Integer isread;

    /**
     * 连接地址
     */
    private String route;

    /**
     * 是否假删除
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
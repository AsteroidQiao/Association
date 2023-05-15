package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName organizer
 */
@TableName(value ="organizer")
@Data
public class Organizer implements Serializable {
    /**
     * 
     */
    @TableId
    private String user;

    /**
     * 
     */

    private String activity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
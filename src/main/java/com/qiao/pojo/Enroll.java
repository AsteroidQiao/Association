package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName enroll
 */
@TableName(value ="enroll")
@Data
public class Enroll implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动ID
     */
    private Integer acid;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 是否录取(0/1判断是否录取)
     */
    private Integer en;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
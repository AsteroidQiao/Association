package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName major
 */
@TableName(value ="major")
@Data
public class Major implements Serializable {
    /**
     * 专业ID
     */
    @TableId(type = IdType.AUTO)
    private Integer mid;

    /**
     * 外键，学院名称
     */
    private String cname;

    /**
     * 专业名称
     */
    private String major;

    /**
     * 不能编辑是否为真（0/1判断）
     */
    private Integer edit;

    /**
     * 假删除（0/1判断）
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName relation
 */
@TableName(value ="relation")
@Data
public class Relation implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 外键 学生ID
     */
    private Integer uid;

    /**
     * 外键 社团ID
     */
    private Integer aid;

    /**
     * 学生职位（通过relation表建立学生和社团关系）
     */
    private String jname;

    /**
     * 是否是社团骨干（0/1判断社团骨干和普通社员）
     */
    private Integer isres;

    /**
     * 是否禁用
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
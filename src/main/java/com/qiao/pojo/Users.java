package com.qiao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName users
 */
@TableName(value = "users")
@Data
public class Users implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer uid;

    /**
     * 用户角色（学生，老师，管理员）
     */
    private String urole;

    /**
     * 用户名
     */
    private String uaccount;

    /**
     * 登录密码
     */
    private String upwd;

    /**
     * 昵称
     */
    private String uname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 学分
     */
    private Integer credit;

    /**
     * 学号
     */
    private String num;

    /**
     * 电话（20位以内）
     */
    private String phone;

    /**
     * 邮箱（绑定账户）
     */
    private String email;

    /**
     * 入学年份
     */
    private Date enrolltime;

    /**
     * 出生日期
     */
    private Date brithday;

    /**
     * 住址
     */
    private String location;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 个人头像
     */
    private String url;

    /**
     * 外键 学院ID
     */
    private String college;

    /**
     * 外键  专业ID
     */
    private String major;

    /**
     * 逻辑删除
     */
    private Integer isdelete;

    /**
     * 密保问题
     */
    private String issue;

    /**
     * 密保答案
     */
    private String answer;
    /**
     * 密保答案
     */
    @TableField(exist = false)

    private String token;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
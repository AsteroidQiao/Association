package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qiao.config.ResponseResult;
import com.qiao.pojo.Users;
import com.qiao.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author AsteroidQiao
 * @Create 2023-02-23
 */
@RestController
@RequestMapping("/UserController")
public class UsersController {
    @Autowired
    private UsersService service;

    /**
     * 登录接口 登录成功后分配给用户一个token，用以判别是否登录
     *
     * @param uaccount
     * @param upwd
     * @return
     */
    @PostMapping("/UserLogin")
    public ResponseResult UserLogin(String uaccount, String upwd, String urole) {
        //queryWrapper 测试 ，使用private UsersService service; 可以自定义sql
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount).eq("upwd", upwd);
        Users one = service.getBaseMapper().selectOne(usersQueryWrapper);
        if (one != null) {
            //String token = JWTUtils.setToken(one.getUid().toString(), upwd);
            //one.setToken(token);
            if (one.getUrole().equals(urole))
                return ResponseResult.okResult(200, "登录成功，欢迎" + uaccount, one);
            else
                return ResponseResult.okResult(201, "操作非法，您不是" + urole + "! 请重新选择");
        } else {
            return ResponseResult.okResult(201, "登录失败,请检查密码或账户是否正确！");
        }
    }

    /**
     * 注册接口
     *
     * @param uaccount
     * @param upwd
     * @param email
     * @return
     */
    @PostMapping("/UserRegister")
    public ResponseResult UserRegister(String uaccount, String upwd, String email) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount);
        Users one = service.getBaseMapper().selectOne(usersQueryWrapper);
        if (one == null) {
            Users user = new Users();
            user.setUaccount(uaccount);//用户账号名，不可修改，不可重复
            user.setUname(uaccount);//用户名，先设置uname和uaccount一致，用于之后可以修改
            user.setUpwd(upwd);//用户密码
            user.setEmail(email);//用户email
            user.setUrole("学生");//新账户默认为学生
            int i = service.getBaseMapper().insert(user);
            if (i != 0) {
                return ResponseResult.okResult(200, "注册成功");
            } else {
                return ResponseResult.okResult(201, "注册失败");
            }
        } else {
            return ResponseResult.okResult(201, "用户已存在！");//用户账号名不可重复
        }
    }

    /**
     * 修改密码，检验用户传递旧密码来更改新密码
     * @param uaccount
     * @param oldpwd
     * @param newpwd
     * @return
     */
    @PostMapping("/UserUpdatepwd")
    public ResponseResult UserUpdatepwd(String uaccount, String oldpwd, String newpwd) {
        UpdateWrapper<Users> updatepwdWrapper = new UpdateWrapper<>();
        updatepwdWrapper.eq("uaccount", uaccount).eq("upwd", oldpwd).set("upwd", newpwd);
        boolean update = service.update(null, updatepwdWrapper);
        if (update) {
            return ResponseResult.okResult(200, "修改成功，请重新登陆");
        } else {
            return ResponseResult.okResult(201, "修改失败，密码输入错误");
        }
    }
}

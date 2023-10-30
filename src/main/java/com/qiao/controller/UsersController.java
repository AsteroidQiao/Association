package com.qiao.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.config.ResponseResult;
import com.qiao.pojo.Users;
import com.qiao.service.UsersService;
import com.qiao.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        usersQueryWrapper.eq("uaccount", uaccount).eq("upwd", upwd).eq("isdelete", 0);
        Users one = service.getBaseMapper().selectOne(usersQueryWrapper);
        if (one != null) {
            //用户登录时分配token
            String token = JWTUtils.setToken(one.getUaccount(), upwd);
            one.setToken(token);
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
     * @param urole
     * @return
     */
    @PostMapping("/UserRegister")
    public ResponseResult UserRegister(String uaccount, String upwd, String urole) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount);
        Users one = service.getBaseMapper().selectOne(usersQueryWrapper);
        if (one == null) {
            Users user = new Users();
            user.setUaccount(uaccount);//用户账号名，不可修改，不可重复
            user.setUname(uaccount);//用户名，先设置uname和uaccount一致，用于之后可以修改
            user.setUpwd(upwd);//用户密码
            user.setEnrolltime(DateUtil.date());//用户注册时间 为当前
            //user.setUrl("http://127.0.0.1:9090/file/cec51ce1bbf34d4b9db1883e8131ceef.png");//默认头像
            //腾讯云
            user.setUrl("https://images.cnblogs.com/cnblogs_com/blogs/796288/galleries/2319977/o_230620090846_%E6%88%90%E5%A4%A7%E4%BA%8B.jpg");//默认头像
            if (Objects.equals(urole, "学生")) {
                user.setUrole(urole);//学生注册
                service.getBaseMapper().insert(user);
                return ResponseResult.okResult(200, "注册成功");
            } else if (Objects.equals(urole, "教师")) {
                user.setUrole(urole);//教师注册
                user.setIsdelete(1);//禁用新注册的教师，等待管理员处理注册申请
                service.getBaseMapper().insert(user);
                return ResponseResult.okResult(200, "注册成功，请等待管理员审核");
            }
        } else {
            return ResponseResult.okResult(201, "用户已存在！");//用户账号名不可重复
        }
        return ResponseResult.okResult();
    }

    /**
     * 修改密码，检验用户传递旧密码来更改新密码
     *
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

    @PostMapping("/UserUpdatepwd2")
    //忘记密码修改密码=>不知道旧密码
    public ResponseResult UserUpdatepwd(String uaccount, String newpwd) {
        UpdateWrapper<Users> updatepwdWrapper = new UpdateWrapper<>();
        updatepwdWrapper.eq("uaccount", uaccount).set("upwd", newpwd);
        boolean update = service.update(null, updatepwdWrapper);
        if (update) {
            return ResponseResult.okResult(200, "修改成功，请重新登陆");
        } else {
            return ResponseResult.okResult(201, "修改失败，密码输入错误");
        }
    }

    @PostMapping("/UserForget")
    public ResponseResult UserForget(String uaccount) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount);
        Users one = service.getOne(usersQueryWrapper);
        if (one != null) {
            return ResponseResult.okResult(200, "操作成功", one);
        }
        return ResponseResult.okResult();
    }

    @PostMapping("/UserUpdate")
    public ResponseResult UserUpdate(@RequestBody Users users) {
        UpdateWrapper<Users> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", users.getUid());
        service.update(users, updateWrapper);
        return ResponseResult.okResult(200, "修改成功");
    }

    @GetMapping("/findAll")
    public ResponseResult findAll() {
        //教师查找没有被假删除的
        QueryWrapper<Users> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(service.list(QueryWrapper));
    }

    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        //管理员查找所有的，包括假删除的
        return ResponseResult.okResult(service.list());
    }

    @GetMapping("/{id}")
    public ResponseResult findById(@PathVariable Integer id) {
        Users user = service.getById(id);
        return ResponseResult.okResult(200, "查找成功", user);
    }

    @GetMapping("/uaccount")
    public ResponseResult findByUaccount(String uaccount) {
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("uaccount", uaccount);
        Users user = service.getOne(usersQueryWrapper);
        return ResponseResult.okResult(200, "查找成功", user);
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name,
                                   @RequestParam String college,
                                   @RequestParam String role) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "uaccount", name).like(StringUtils.isNotBlank(role), "urole", role).like(StringUtils.isNotBlank(college), "college", college);
        return ResponseResult.okResult(service.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //分页展示
    @GetMapping("/credit")
    public ResponseResult credit(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam Integer max,
                                 @RequestParam Integer min,
                                 @RequestParam String name,
                                 @RequestParam String college,
                                 @RequestParam String num) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("urole", "学生").eq("isdelete", 0).like("uaccount", name).like("num", num).like("college", college);
        queryWrapper.between("credit", min, max);
        return ResponseResult.okResult(service.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

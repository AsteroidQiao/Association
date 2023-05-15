package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Users;
import com.qiao.service.EnrollService;
import com.qiao.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Enroll;
import com.qiao.config.ResponseResult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/enroll")
public class EnrollController {

    @Autowired
    private EnrollService enrollService;
    @Autowired
    private UsersService usersService;

    // 新增
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Enroll enroll) {
        //如果已报名，提示已经报名
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", enroll.getUid());
        queryWrapper.eq("acid", enroll.getAcid());
        Enroll one = enrollService.getOne(queryWrapper);
        if (one != null) {
            return ResponseResult.okResult(201, "您已报名");
        }
        //未报名，则加入数据
        else {
            enrollService.save(enroll);
            return ResponseResult.okResult(200, "报名成功");
        }
    }

    // 录取
    @PostMapping("/enroll")
    public ResponseResult enroll(@RequestBody Enroll enroll) {
        UpdateWrapper<Enroll> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", enroll.getUid());
        updateWrapper.eq("acid", enroll.getAcid());
        updateWrapper.set("en", enroll.getEn());
        enrollService.update(updateWrapper);
        return ResponseResult.okResult(200, "报名状态已修改");
    }

    //完全删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        enrollService.removeById(id);
        return ResponseResult.okResult();
    }

    //逻辑删除 (假删除)
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<Enroll> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        enrollService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        enrollService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    //管理员查找所有的记录，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(enrollService.list());
    }

    //查询没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        QueryWrapper<Enroll> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(enrollService.list(QueryWrapper));
    }

    //查询报名状态
    @GetMapping("/findAllById")
    public ResponseResult findAllById(String acid) {
        QueryWrapper<Enroll> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("acid", acid);
        return ResponseResult.okResult(enrollService.list(QueryWrapper));
    }

    //查询活动报名成员
    @GetMapping("/findAllByAcid")
    public ResponseResult findAllByName(Integer acid) {
        //通过活动id查询enroll表中的uid
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.eq("acid", acid);
        List<Enroll> list = enrollService.list(enrollQueryWrapper);
        List<Integer> uids = new ArrayList<>();
        for (Enroll enroll : list) {
            uids.add(enroll.getUid());
        }
        //通过uid在user表查询用户详细详细
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.in("uid", uids);
        List<Users> users = usersService.list(usersQueryWrapper);
        return ResponseResult.okResult(200, users);
    }

    //通过id查询
    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(enrollService.getById(id));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Enroll> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return ResponseResult.okResult(enrollService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //查询活动报名成员
    @GetMapping("/findAllByAcidPage")
    public ResponseResult findAllByNamePage(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize,
                                            @RequestParam String name,
                                            @RequestParam Integer acid,
                                            @RequestParam Integer en) {
        //通过活动id查询enroll表中的uid
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.eq("acid", acid);
        if (!en.equals(2)) {
            enrollQueryWrapper.eq("en", en);
        }
        List<Enroll> list = enrollService.list(enrollQueryWrapper);
        //如果list为空，直接返回
        if (list.isEmpty()) {
            return ResponseResult.okResult(201,"未查找到该类型用户");
        } else {
            List<Integer> uids = new ArrayList<>();
            for (Enroll enroll : list) {
                uids.add(enroll.getUid());
            }
            //通过uid在user表查询用户详细详细
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.in("uid", uids);
            usersQueryWrapper.like("uaccount", name);
            //List<Users> users = usersService.list(usersQueryWrapper);
            return ResponseResult.okResult(usersService.page(new Page<>(pageNum, pageSize), usersQueryWrapper));
        }
    }

}


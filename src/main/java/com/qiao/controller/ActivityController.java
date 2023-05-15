package com.qiao.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Enroll;
import com.qiao.service.ActivityService;
import com.qiao.service.EnrollService;
import com.qiao.service.UsersService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Activity;
import com.qiao.config.ResponseResult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private EnrollService enrollService;

    // 新增活动
    @PostMapping("/add")
    public ResponseResult save(@RequestBody Activity activity) {
        QueryWrapper<Activity> activityQueryWrapper = new QueryWrapper<>();
        activityQueryWrapper.eq("name", activity.getName());
        Activity one = activityService.getOne(activityQueryWrapper);
        //如果活动已存在
        if (one != null) {
            return ResponseResult.okResult(201, "活动已存在！");
        } else {
            //////如果是教师或者管理员发布活动，直接通过
            //if (urole.equals("教师||管理员")) {
            //    activity.setPass(1);
            //}
            activityService.save(activity);
            return ResponseResult.okResult(200, "添加成功!", activityService.getOne(activityQueryWrapper));
        }
    }

    // 修改活动
    @PostMapping("/update")
    public ResponseResult update(@RequestBody Activity activity) {
        activityService.saveOrUpdate(activity);
        return ResponseResult.okResult(200, "修改成功!");
    }

    //完全删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        activityService.removeById(id);
        return ResponseResult.okResult();
    }

    //逻辑删除 (假删除)
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<Activity> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        activityService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        activityService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    //管理员查找所有的记录，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(activityService.list());
    }

    //查询没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        QueryWrapper<Activity> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(activityService.list(QueryWrapper));
    }

    //查询热门活动
    @GetMapping("/findHot")
    public ResponseResult findHot() {
        QueryWrapper<Activity> QueryWrapper = new QueryWrapper<>();
        //近期活动
        Date now = DateUtil.date();
        // 获取当前时间
        System.out.println(now);
        QueryWrapper.gt("endtime", now);// 查询活动结束时间大于当前时间
        //活动排名靠前的12个活动
        QueryWrapper.between("acid", 1, 12);
        //活动已通过
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(activityService.list(QueryWrapper));
    }

    //查询社团下所有活动
    @GetMapping("/findByClub")
    public ResponseResult findByActivity(String club) {
        QueryWrapper<Activity> QueryWrapper = new QueryWrapper<>();
        //查询社团名称
        QueryWrapper.eq("association", club);//查询社团名称
        //活动已通过
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(activityService.list(QueryWrapper));
    }

    //通过id查询
    @GetMapping("/id")
    public ResponseResult findById(Integer id) {
        return ResponseResult.okResult(activityService.getById(id));
    }

    //通过用户名
    @GetMapping("/name")
    public ResponseResult findByName(String name) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return ResponseResult.okResult(activityService.getOne(queryWrapper));
    }

    //模糊查询
    @GetMapping("/search")
    public ResponseResult search(@RequestParam String keywords) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keywords);
        return ResponseResult.okResult(200, "查找成功", activityService.list(queryWrapper));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name,
                                   @RequestParam Integer pass) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        if (pass == 2) {
            queryWrapper.like("name", name);
        } else {
            queryWrapper.like("name", name).like("pass", pass);
        }
        queryWrapper.orderByAsc("endtime");
        return ResponseResult.okResult(activityService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //分页展示
    @GetMapping("/myActivity")
    public ResponseResult myActivity(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam String name,
                                     @RequestParam Integer uid) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        //通过uid查找enroll表的活动acid
        QueryWrapper<Enroll> enrollQueryWrapper = new QueryWrapper<>();
        enrollQueryWrapper.eq("uid", uid);
        List<Enroll> enrolls = enrollService.list(enrollQueryWrapper);
        List<Integer> acids = new ArrayList<>();
        for (Enroll en : enrolls
        ) {
            acids.add(en.getAcid());
        }
        //通过acid 在activity表查找活动
        queryWrapper.like("name", name).eq("pass", 1).in("acid", acids);
        queryWrapper.orderByAsc("endtime");
        return ResponseResult.okResult(activityService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


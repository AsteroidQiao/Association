package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Notification;
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
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 新增或者更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Notification notification) {
        notificationService.saveOrUpdate(notification);
        return ResponseResult.okResult();
    }

    //完全删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        notificationService.removeById(id);
        return ResponseResult.okResult();
    }

    //逻辑删除 (假删除)
    @PostMapping ("/delete")
    public ResponseResult delete(Integer id) {
        UpdateWrapper<Notification> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        notificationService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        notificationService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    //管理员查找所有的记录，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(notificationService.list());
    }

    //查询没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll(Integer uid) {
        QueryWrapper<Notification> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("uid", uid);
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(notificationService.list(QueryWrapper));
    }

    //通过id查询
    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(notificationService.getById(id));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name,
                                   @RequestParam Integer uid) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type", name);
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(notificationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


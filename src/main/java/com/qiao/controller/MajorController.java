package com.qiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.College;
import com.qiao.pojo.Major;
import com.qiao.service.MajorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.config.ResponseResult;


import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    // 新增或者更新
    @PostMapping
    public ResponseResult save(@RequestBody Major major) {
        //判断专业是否存在
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("major", major.getMajor()).eq("cname",major.getCname());
        Major one = majorService.getOne(queryWrapper);
        if (one != null)//专业存在发出警告
        {
            return ResponseResult.okResult(201, "警告该学院已存在此专业！");
        } else {
            majorService.saveOrUpdate(major);
            return ResponseResult.okResult();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        majorService.removeById(id);
        return ResponseResult.okResult();
    }

    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        majorService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping
    public ResponseResult findAll() {
        return ResponseResult.okResult(majorService.list());
    }

    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(majorService.getById(id));
    }

    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Major> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("major", name).or().like("cname", name);
        queryWrapper.orderByDesc("cname");
        return ResponseResult.okResult(majorService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


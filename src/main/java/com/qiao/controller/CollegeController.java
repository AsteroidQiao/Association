package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.College;
import com.qiao.service.CollegeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.config.ResponseResult;


import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    // 新增或修改
    @PostMapping
    public ResponseResult save(@RequestBody College college) {
        //判断学院是否存在
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        //学院存在
        queryWrapper.eq("cname", college.getCname());
        College one = collegeService.getOne(queryWrapper);
        if (one != null)//学院存在发出警告
        {
            //学院存在就只能更新 isdelete状态
            if (!Objects.equals(one.getIsdelete(), college.getIsdelete())) {
                UpdateWrapper<College> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("cid", college.getCid()).set("isdelete", college.getIsdelete());
                college.setCname(one.getCname());
                collegeService.update(updateWrapper);
                return ResponseResult.okResult(200, "假删除状态更新！");
            }
            //未更新发出警告
            else {
                return ResponseResult.okResult(201, "警告该学院已存在！");
            }
        } else {
            collegeService.saveOrUpdate(college);
            return ResponseResult.okResult();
        }
    }


    // 修改
    @PostMapping("/update")
    public ResponseResult Update(@RequestParam Integer cid, @RequestParam String cname) {
        College college = new College();
        college.setCname(cname);
        UpdateWrapper<College> collegeUpdateWrapper = new UpdateWrapper<>();
        collegeUpdateWrapper.set("cname", cname).eq("cid", cid);
        collegeService.update(collegeUpdateWrapper);
        return ResponseResult.okResult(200, "操作成功");
    }

    //教师 假删除
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<College> collegeUpdateWrapper = new UpdateWrapper<>();
        collegeUpdateWrapper.set("isdelete", 1).eq("cid", id);
        collegeService.update(collegeUpdateWrapper);
        return ResponseResult.okResult();
    }

    //管理员可以完全删除
    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        collegeService.removeById(id);
        return ResponseResult.okResult();
    }


    //教师查找没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
        collegeQueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(collegeService.list(collegeQueryWrapper));
    }

    //管理员查找所有的，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(collegeService.list());
    }

    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(collegeService.getById(id));
    }

    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        //教师查找没有被假删除的
        queryWrapper.like("cname", name).eq("isdelete", 0);
        queryWrapper.orderByDesc("cid");
        return ResponseResult.okResult(collegeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/pageAdmin")
    public ResponseResult findPageAdmin(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam String name) {
        QueryWrapper<College> queryWrapper = new QueryWrapper<>();
        //管理员查找所有的
        queryWrapper.like("cname", name);
        queryWrapper.orderByDesc("cname");
        return ResponseResult.okResult(collegeService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


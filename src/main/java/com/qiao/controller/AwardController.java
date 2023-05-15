package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Picture;
import com.qiao.service.AwardService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Award;
import com.qiao.config.ResponseResult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-04-17
 */
@RestController
@RequestMapping("/award")
public class AwardController {

    @Autowired
    private AwardService awardService;

    // 新增或者更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Award award) {
        awardService.saveOrUpdate(award);
        return ResponseResult.okResult();
    }

    // 批量新增
    @PostMapping("/addBatch")
    public ResponseResult addBatch(@RequestBody ArrayList<Award> award) {
        awardService.saveBatch(award);
        return ResponseResult.okResult();
    }

    //完全删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        awardService.removeById(id);
        return ResponseResult.okResult();
    }

    //逻辑删除 (假删除)
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<Award> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        awardService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(String name) {
        QueryWrapper<Award> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("activity", name);
        awardService.getBaseMapper().delete(QueryWrapper);
        return ResponseResult.okResult();
    }

    //管理员查找所有的记录，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(awardService.list());
    }

    //查询没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        QueryWrapper<Award> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(awardService.list(QueryWrapper));
    }

    //查询活动奖励
    @GetMapping("/findAllByName")
    public ResponseResult findAllByName(String name) {
        QueryWrapper<Award> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("activity", name);
        return ResponseResult.okResult(awardService.list(QueryWrapper));
    }

    //通过id查询
    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(awardService.getById(id));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Award> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return ResponseResult.okResult(awardService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


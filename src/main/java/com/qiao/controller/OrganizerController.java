package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Picture;
import com.qiao.service.OrganizerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Organizer;
import com.qiao.config.ResponseResult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/organizer")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    // 新增
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Organizer organizer) {
        organizerService.save(organizer);
        return ResponseResult.okResult();
    }

    //完全删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        organizerService.removeById(id);
        return ResponseResult.okResult();
    }

    //逻辑删除 (假删除)
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<Organizer> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        organizerService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(String name) {
        QueryWrapper<Organizer> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper.eq("activity", name);
        organizerService.getBaseMapper().delete(pictureQueryWrapper);
        return ResponseResult.okResult();
    }

    //管理员查找所有的记录，包括假删除的
    @GetMapping("/findAllAdmin")
    public ResponseResult findAllAdmin() {
        return ResponseResult.okResult(organizerService.list());
    }

    //查询没有被假删除的
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        QueryWrapper<Organizer> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(organizerService.list(QueryWrapper));
    }

    //查询活动下的所有组织者
    @GetMapping("/findAllByName")
    public ResponseResult findAllByName(String name) {
        QueryWrapper<Organizer> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("activity", name);
        return ResponseResult.okResult(organizerService.list(QueryWrapper));
    }

    //通过id查询
    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(organizerService.getById(id));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Organizer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return ResponseResult.okResult(organizerService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


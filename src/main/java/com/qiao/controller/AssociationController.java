package com.qiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Association;
import com.qiao.service.AssociationService;
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
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/association")
public class AssociationController {

    @Autowired
    private AssociationService associationService;

    // 新增或者更新
    @PostMapping
    public ResponseResult save(@RequestBody Association association) {
        associationService.saveOrUpdate(association);
        return ResponseResult.okResult();
    }

    @PostMapping("/insert")
    public ResponseResult insert(@RequestParam String name, @RequestParam String info, @RequestParam String icon, @RequestParam String college) {
        Association association = new Association();
        //检查社团名称是否重复
        QueryWrapper<Association> associationQueryWrapper = new QueryWrapper<>();
        associationQueryWrapper.eq("name", name);
        Association one = associationService.getOne(associationQueryWrapper);
        //如果重复，提示警告信息
        if (one != null) {
            return ResponseResult.okResult(201, "警告!该社团已存在！请换个名称");
        } else {
            association.setName(name);
            association.setInfo(info);
            association.setIcon(icon);
            association.setCollege(college);
            associationService.save(association);
            return ResponseResult.okResult();
        }

    }


    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        associationService.removeById(id);
        return ResponseResult.okResult();
    }

    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        associationService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping
    public ResponseResult findAll() {
        return ResponseResult.okResult(associationService.list());
    }

    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(associationService.getById(id));
    }

    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Association> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return ResponseResult.okResult(associationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


package com.qiao.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Activity;
import com.qiao.pojo.Association;
import com.qiao.pojo.Relation;
import com.qiao.service.AssociationService;
import com.qiao.service.RelationService;
import com.qiao.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
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
    private RelationService relationService;

    @Autowired
    private UsersService usersService;
    @Autowired
    private AssociationService associationService;

    // 新增或者更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Association association) {
        associationService.saveOrUpdate(association);
        return ResponseResult.okResult();
    }

    @PostMapping("/insert")
    public ResponseResult insert(@RequestParam String name, @RequestParam String info, @RequestParam String icon,
                                 @RequestParam String college) {
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
            return ResponseResult.okResult(200, "添加成功");
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

    @GetMapping("/findHot")
    public ResponseResult findHot() {
        QueryWrapper<Association> QueryWrapper = new QueryWrapper<>();
        //活动排名靠前的5个部落
        QueryWrapper.between("id", 1, 6);
        //活动已通过
        //QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(associationService.list(QueryWrapper));
    }

    @GetMapping("/id")
    //通过ID查找
    public ResponseResult findOne(Integer id) {
        return ResponseResult.okResult(associationService.getById(id));
    }

    //通过用户名
    @GetMapping("/name")
    public ResponseResult findByName(String name) {
        QueryWrapper<Association> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return ResponseResult.okResult(200, "操作成功", associationService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Association> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return ResponseResult.okResult(associationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/myAssociation")
    public ResponseResult myAssociation(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam String name,
                                        @RequestParam Integer uid) {
        //通过uid查找relation表的aid
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("uid", uid).eq("isdelete", 0);
        List<Relation> list = relationService.list(relationQueryWrapper);
        List<Integer> aid = new ArrayList<>();
        for (Relation relation : list
        ) {
            aid.add(relation.getAid());
        }
        //通过aid查找社团
        QueryWrapper<Association> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name).in("id", aid);
        return ResponseResult.okResult(associationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/myAssociation2")
    public ResponseResult myAssociation2(@RequestParam Integer uid) {
        //通过uid查找relation表的aid
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("uid", uid).eq("isdelete", 0);
        List<Relation> list = relationService.list(relationQueryWrapper);
        List<Integer> aid = new ArrayList<>();
        for (Relation relation : list
        ) {
            aid.add(relation.getAid());
        }
        //通过aid查找社团
        if (aid.isEmpty()) {
            return ResponseResult.okResult(201, "用户未加入社团");
        } else {
            QueryWrapper<Association> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", aid);
            return ResponseResult.okResult(associationService.list(queryWrapper));
        }
    }

}


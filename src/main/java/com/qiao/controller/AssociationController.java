package com.qiao.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Activity;
import com.qiao.pojo.Association;
import com.qiao.pojo.Relation;
import com.qiao.service.AssociationService;
import com.qiao.service.RelationService;
import com.qiao.service.UsersService;
import org.apache.poi.util.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 新增或者更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Association association) {
        associationService.saveOrUpdate(association);
        //修改=>刷新缓存
        flushRedis("associationRedis");
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
            //新增=>刷新缓存
            flushRedis("associationRedis");
            return ResponseResult.okResult(200, "添加成功");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        associationService.removeById(id);
        //删除时=>刷新缓存
        flushRedis("associationRedis");
        return ResponseResult.okResult();
    }

    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        associationService.removeByIds(ids);
        //删除时=>刷新缓存
        flushRedis("associationRedis");
        return ResponseResult.okResult();
    }

    @GetMapping
    public ResponseResult findAll() {
        return ResponseResult.okResult(associationService.list());
    }

    @GetMapping("/findHot")
    public ResponseResult findHot() {
        // 1. 从缓存获取数据
        String jsonStr = stringRedisTemplate.opsForValue().get("associationRedis");
        List<Association> associationRedis;
        if (StrUtil.isBlank(jsonStr)) { // 2. 取出来的json是空的
            QueryWrapper<Association> QueryWrapper = new QueryWrapper<>();
            //推荐部落
            // 活动排名靠前的6个部落
            QueryWrapper.between("id", 1, 6);
            associationRedis = associationService.list(QueryWrapper);// 3. 从数据库取出数据
            // 4. 再去缓存到redis
            stringRedisTemplate.opsForValue().set("associationRedis", JSONUtil.toJsonStr(associationRedis));
        }
        else {
            associationRedis=JSONUtil.toBean(jsonStr, new TypeReference<List<Association>>() {
            }, true);
        }

        //活动已通过
        //QueryWrapper.eq("isdelete", 0);
        return ResponseResult.okResult(associationRedis);
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

    // 删除缓存
    private void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }
}


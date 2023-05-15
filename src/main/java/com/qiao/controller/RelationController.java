package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.pojo.Association;
import com.qiao.pojo.Enroll;
import com.qiao.pojo.Relation;
import com.qiao.pojo.Users;
import com.qiao.service.AssociationService;
import com.qiao.service.RelationService;
import com.qiao.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AssociationService associationService;

    // 新增或者更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Relation relation) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", relation.getUid());
        queryWrapper.eq("aid", relation.getAid());
        Relation one = relationService.getOne(queryWrapper);
        if (one != null) {
            return ResponseResult.okResult(201, "您已在该社团！");
        } else {
            relationService.saveOrUpdate(relation);
            return ResponseResult.okResult(200, "加入成功");
        }
    }

    // 录取
    @PostMapping("/enroll")
    public ResponseResult enroll(@RequestBody Relation enroll) {
        UpdateWrapper<Relation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", enroll.getUid());
        updateWrapper.eq("aid", enroll.getAid());
        updateWrapper.set("isdelete", enroll.getIsdelete());
        relationService.update(updateWrapper);
        return ResponseResult.okResult(200, "录取状态已修改");
    }

    //通过ID删除
    @DeleteMapping("/{id}")
    public ResponseResult deleteAdmin(@PathVariable Integer id) {
        relationService.removeById(id);
        return ResponseResult.okResult();
    }

    //假删除
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        UpdateWrapper<Relation> UpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper.set("isdelete", 1).eq("id", id);
        relationService.update(UpdateWrapper);
        return ResponseResult.okResult();
    }

    //批量删除
    @PostMapping("/del/batch")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        relationService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    //查找社团下的所有成员
    @GetMapping("/findAll")
    public ResponseResult findAll(String name) {
        //通过社团名称，查找该社团全部信息
        QueryWrapper<Association> associationQueryWrapper = new QueryWrapper<>();
        associationQueryWrapper.eq("name", name);
        Association one = associationService.getOne(associationQueryWrapper);
        //通过社团id，查找relation表中用户uid
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("aid", one.getId());
        //查找未禁用的用户
        relationQueryWrapper.eq("isdelete", 0);
        List<Relation> list = relationService.list(relationQueryWrapper);
        List<Integer> uids = new ArrayList<>();
        for (Relation relation : list) {
            uids.add(relation.getUid());
        }
        //如果社团成员为空，提示需要新加社员才能发布活动
        if (uids.isEmpty()) {
            return ResponseResult.okResult(201, "成员为空，请先添加社员！");
        } else {
            //通过uids查找用户信息
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.in("uid", uids);
            List<Users> users = usersService.list(usersQueryWrapper);
            return ResponseResult.okResult(200, users);
        }
    }

    //查找社团下的所有成员
    @GetMapping("/findAllByAid")
    public ResponseResult findAll(Integer aid) {
        //通过社团id，查找relation表中用户uid
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("aid", aid);
        //查找未禁用的用户
        relationQueryWrapper.eq("isdelete", 0);
        List<Relation> list = relationService.list(relationQueryWrapper);
        List<Integer> uids = new ArrayList<>();
        for (Relation relation : list) {
            uids.add(relation.getUid());
        }
        //如果社团成员为空，提示需要新加社员才能发布活动
        if (uids.isEmpty()) {
            return ResponseResult.okResult(201, "成员为空，请先添加社员！");
        } else {
            //通过uids查找用户信息
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.in("uid", uids);
            List<Users> users = usersService.list(usersQueryWrapper);
            return ResponseResult.okResult(200, users);
        }
    }


    //校验是否是社团负责人
    @GetMapping("/isOrgan")
    public ResponseResult isOrgan(Integer aid, Integer uid) {
        //通过社团id，查找relation表中用户uid
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.eq("aid", aid);
        relationQueryWrapper.eq("uid", uid);
        relationQueryWrapper.eq("isres", 1);
        //查找未禁用的用户
        relationQueryWrapper.eq("isdelete", 0);
        Relation one = relationService.getOne(relationQueryWrapper);
        //如果是管理员，即one存在
        if (one != null) {
            return ResponseResult.okResult(200, "校验是社团管理人员", true);
        } else return ResponseResult.okResult(201, "校验不是社团管理人员", false);
    }

    //通过id查询
    @GetMapping("/{id}")
    public ResponseResult findOne(@PathVariable Integer id) {
        return ResponseResult.okResult(relationService.getById(id));
    }

    //分页展示
    @GetMapping("/page")
    public ResponseResult findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam String name) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return ResponseResult.okResult(relationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    //通过uid查找社团下的所有成员
    @GetMapping("/findAllByAidPage")
    public ResponseResult findAllByAid(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam String name,
                                       @RequestParam Integer aid,
                                       @RequestParam Integer enable) {
        QueryWrapper<Relation> relationQueryWrapper = new QueryWrapper<>();
        //通过社团id，查找relation表中用户uid
        relationQueryWrapper.eq("aid", aid);
        //查找未禁用的用户
        if (!enable.equals(2)) {
            relationQueryWrapper.eq("isdelete", enable);
        }
        List<Relation> list = relationService.list(relationQueryWrapper);
        //如果list为空，直接返回
        if (list.isEmpty()) {
            return ResponseResult.okResult(201,"未查找到该类型用户");
        } else {
            List<Integer> uids = new ArrayList<>();
            for (Relation relation : list) {
                uids.add(relation.getUid());
            }
            //通过uid在user表查询用户详细详细
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.in("uid", uids);
            usersQueryWrapper.like("uaccount", name);
            return ResponseResult.okResult(usersService.page(new Page<>(pageNum, pageSize), usersQueryWrapper));
        }
    }
}


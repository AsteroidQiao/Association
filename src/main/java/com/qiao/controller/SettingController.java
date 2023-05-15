package com.qiao.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiao.service.SettingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiao.pojo.Setting;
import com.qiao.config.ResponseResult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 乔羽
 * @since 2023-05-03
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    // 更新
    @PostMapping("/save")
    public ResponseResult save(@RequestBody Setting setting) {
        settingService.saveOrUpdate(setting);
        return ResponseResult.okResult(200, "保存成功");
    }

    //完全删除
    @PostMapping("/select")
    public ResponseResult select() {
        QueryWrapper<Setting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ID", 1);
        Setting one = settingService.getBaseMapper().selectOne(queryWrapper);
        return ResponseResult.okResult(200, "查找成功", one);
    }

}


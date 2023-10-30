package com.qiao.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.qiao.config.ResponseResult;
import com.qiao.pojo.Users;
import com.qiao.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author AsteroidQiao
 * @Create 2023-04-13
 */
@RestController
@RequestMapping("/charts")
public class EchartsController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/UserChart")
    public ResponseResult UserChart() {
        List<Users> users = usersService.list();
        int Q1 = 0;
        int Q2 = 0;
        int Q3 = 0;
        int Q4 = 0;
        for (Users user : users) {
            //获取用户注册时间
            Date date = user.getEnrolltime();
            Quarter quarter = DateUtil.quarterEnum(date);
            switch (quarter) {
                case Q1:
                    Q1 += 1;
                    break;
                case Q2:
                    Q2 += 1;
                    break;
                case Q3:
                    Q3 += 1;
                    break;
                case Q4:
                    Q4 += 1;
                    break;
                default:
                    break;
            }
        }
        return ResponseResult.okResult(CollUtil.newArrayList(Q1, Q2, Q3, Q4));
    }
}

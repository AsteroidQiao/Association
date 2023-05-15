package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Notification;
import com.qiao.service.NotificationService;
import com.qiao.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【notification】的数据库操作Service实现
* @createDate 2023-05-08 20:33:45
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService{

}





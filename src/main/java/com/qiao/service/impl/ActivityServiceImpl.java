package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Activity;
import com.qiao.service.ActivityService;
import com.qiao.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【activity】的数据库操作Service实现
* @createDate 2023-04-18 23:32:25
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}





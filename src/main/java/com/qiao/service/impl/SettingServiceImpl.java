package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Setting;
import com.qiao.service.SettingService;
import com.qiao.mapper.SettingMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【setting】的数据库操作Service实现
* @createDate 2023-05-03 15:45:19
*/
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting>
    implements SettingService{

}





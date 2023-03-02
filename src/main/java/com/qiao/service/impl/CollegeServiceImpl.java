package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.College;
import com.qiao.service.CollegeService;
import com.qiao.mapper.CollegeMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【college】的数据库操作Service实现
* @createDate 2023-03-02 09:12:48
*/
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College>
    implements CollegeService{

}





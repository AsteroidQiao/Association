package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Users;
import com.qiao.service.UsersService;
import com.qiao.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【users】的数据库操作Service实现
* @createDate 2023-05-09 00:03:41
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}





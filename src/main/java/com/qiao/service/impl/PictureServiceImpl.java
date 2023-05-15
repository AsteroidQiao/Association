package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Picture;
import com.qiao.service.PictureService;
import com.qiao.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【picture】的数据库操作Service实现
* @createDate 2023-04-18 23:33:13
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}





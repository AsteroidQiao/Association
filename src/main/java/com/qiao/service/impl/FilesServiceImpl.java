package com.qiao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiao.pojo.Files;
import com.qiao.service.FilesService;
import com.qiao.mapper.FilesMapper;
import org.springframework.stereotype.Service;

/**
* @author 25518
* @description 针对表【files】的数据库操作Service实现
* @createDate 2023-02-27 20:24:04
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
    implements FilesService{

}





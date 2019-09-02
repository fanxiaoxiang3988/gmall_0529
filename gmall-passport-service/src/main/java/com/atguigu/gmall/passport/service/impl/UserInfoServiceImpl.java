package com.atguigu.gmall.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.passport.mapper.UserInfoMapper;
import com.atguigu.gmall.user.UserInfo;
import com.atguigu.gmall.user.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/23 0023
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public UserInfo login(UserInfo userInfo) {
        String passwd = userInfo.getPasswd();
        //将用户输入的密码转为加密格式，进行查询
        String md5Hex = DigestUtils.md5Hex(passwd);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",userInfo.getLoginName());
        wrapper.eq("passwd",md5Hex);
        UserInfo selectOne = userInfoMapper.selectOne(wrapper);
        return selectOne;
    }

}

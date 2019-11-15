package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.manager.mapper.UserAddressMapper;
import com.atguigu.gmall.user.User;
import com.atguigu.gmall.user.UserAddress;
import com.atguigu.gmall.user.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/14 0014
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserAddressMapper userAddressMapper;




    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public void buyMovie(String uid, String mid) {

    }

    /**
     * 获取用户的收货地址列表
     * @param id
     * @return
     */
    @Override
    public List<UserAddress> getUserAddressesByUserId(int id) {
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        List<UserAddress> userAddresses = userAddressMapper.selectList(wrapper);
        return userAddresses;
    }

}

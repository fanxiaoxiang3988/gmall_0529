package com.atguigu.gmall.manager.mapper;

import com.atguigu.gmall.manager.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/14 0014
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 测试在mapper.xml文件中映射这个方法
     * @param user
     * @return
     */
    public User getUserByHaha(User user);

}

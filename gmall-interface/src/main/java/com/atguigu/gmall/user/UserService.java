package com.atguigu.gmall.user;


/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/10 0010
 */
public interface UserService {

    /**
     * 获取用户
     * @param id
     * @return
     */
    public User getUser(String id);

    /**
     * 购买电影
     * @param uid  用户id
     * @param mid  电影id
     */
    public void buyMovie(String uid,String mid);

}

package com.atguigu.gmall.movie;


import com.atguigu.gmall.user.Movie;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/10 0010
 */
public interface MovieService {

    /**
     * 返回movie
     * @param id
     * @return
     */
    public Movie getMovie(String id);

}

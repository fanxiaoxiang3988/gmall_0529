package com.atguigu.gmall.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.movie.MovieService;
import com.atguigu.gmall.user.Movie;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/11 0011
 */
@Service
public class MovieServiceImpl implements MovieService {
    @Override
    public Movie getMovie(String id) {
        return new Movie("1","完美世界");
    }
}

package com.atguigu.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.movie.MovieService;
import com.atguigu.gmall.user.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/11 0011
 */
@Controller
public class UserController {

    @Reference
    MovieService movieService;


    @ResponseBody
    @RequestMapping("/movie")
    public Movie buyTicket(String userId, String mid){
        Movie movie = movieService.getMovie(mid);

        return movie;
    }

}

package com.atguigu.gmall.manager.components;


import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/21 0021
 */
@Slf4j
@Component
public class FastDFSTemplate {


    //使用@Value注解，可将application.properties中的配置文件的值取出
    @Value("${gmall.file.server}")
    private String fileServer;


    //初始化加载流程只需要加载一次，可放入构造器中，容器创建Bean对象时就会自动加载
    public FastDFSTemplate(){
        //初始化流程
        //1、加载配置文件
        String file = this.getClass().getResource("/tracker.conf").getFile();
        //2、初始化fastdfs客户端配置
        try {
            ClientGlobal.init(file);
        } catch (IOException e) {
            log.info("FastDFS客户端插件初始化异常：{}",e);
        } catch (MyException e) {
            log.info("FastDFS客户端插件初始化异常：{}",e);
        }
    }

    //FastDFS上传文件时，每上传一个文件，都需要获取一个新的StrorageClient对象
    public StorageClient getStorageClient() throws IOException {
        //上传流程
        //1、创建TrackerClient
        TrackerClient trackerClient=new TrackerClient();
        //2、获取到和TrackerServer的连接
        TrackerServer trackerServer=trackerClient.getConnection();
        //3、根据TrackerServer返回的信息创建出操作Storage的客户端
        StorageClient storageClient=new StorageClient(trackerServer,null);
        return storageClient;
    }


    //获取文件的路径
    public String getPath(String[] strings){
        String path = "http://" + fileServer + "/";
        path += strings[0] + "/" + strings[1];
        return path;
    }





}

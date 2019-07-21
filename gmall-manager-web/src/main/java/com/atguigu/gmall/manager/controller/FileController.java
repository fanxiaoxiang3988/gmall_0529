package com.atguigu.gmall.manager.controller;

import com.atguigu.gmall.manager.components.FastDFSTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/20 0020
 */
@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FastDFSTemplate fastDFSTemplate;

    /**
     * MultipartFile  这个类一般是用来接受前台传过来的文件
     * <input type="file" name="file"  multiple/>
     * multiple是指允许多选
     * @RequestParam("file")中file对应的是input框中的name
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            //String name = file.getName();
            String filename = file.getOriginalFilename();//真实的文件名
            //long size = file.getSize();
            //log.info("文件项：{}；名字：{}；大小：{}；上传成功",name,filename,size);
            StorageClient storageClient = fastDFSTemplate.getStorageClient();
            //获取文件的后缀名
            //string自带的工具类，取出所给符号最后出现位置之后的值
            String ext = StringUtils.substringAfterLast(filename, ".");
            try {
                //storageClient自带上传文件功能，第一个参数文件的二进制流，第二个参数文件的后缀名
                String[] strings = storageClient.upload_file(file.getBytes(), ext, null);
                String path = fastDFSTemplate.getPath(strings);
                //log.info("文件上传成功，地址为：{}",path);
                return path;
            } catch (MyException e) {
                log.error("文件上传出错:{}",e);
            }
        }
        return "/images/error.png";
    }









}

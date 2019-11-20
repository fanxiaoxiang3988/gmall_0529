package com.example.demo;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class APPCodeDemo {

    public static void main(String[] args) {
        //API产品路径
        String host = "http://ai-market-ocr-person-id.icredit.link";
        String path = "/ai-market/ocr/personid";
        String method = "POST";
        //阿里云APPCODE
        String appcode = "b4c5e6332bef4ec4a7252f315a0bb7b4";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        //内容数据类型，如：0，则表示BASE64编码；1，则表示图像文件URL链接
        //启用BASE64编码方式进行识别
        //内容数据类型是BASE64编码
        String imgFile = "D:\\timg.jpg";
        String imgBase64 = "";
        try {
            File file = new File(imgFile);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(encodeBase64(content));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        bodys.put("AI_IDCARD_IMAGE", imgBase64);
        bodys.put("AI_IDCARD_IMAGE_TYPE", "0");

        //启用URL方式进行识别
        //内容数据类型是图像文件URL链接
        bodys.put("AI_IDCARD_IMAGE", "D:\\timg.jpg");
        bodys.put("AI_IDCARD_IMAGE_TYPE", "1");

        //身份证正反面，如：FRONT，身份证含照片的一面；BACK，身份证带国徽的一面
        bodys.put("AI_IDCARD_SIDE", "FRONT");

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
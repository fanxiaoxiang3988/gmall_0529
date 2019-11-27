package com.atguigu.gmall.payment.config;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/11/26 0026
 */
public class GmallAlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101600701922";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDxEmdOQWLH94KUmD8mh2/aJx91UA5HSV4aLXi6wLTFcKaifxEdj7ay0tOWR6jw9en5bsl2Sdp2dQS+d6I3GykOzzWZoBNzTgxtuVHOirfC2KduXZVH45v+iYH/GLblCU2pbvyUZOBkg85JrUl3rfTTGWj5ulqVMdB/v5ei8IabI9DniDyg67bWiS4pMYtgRNTZPoD/w31m4XTxi8dZ/Q89yKFZ3PnRVjXKzWIn1iF0gJuU4i7PGJcsU51BbCFl+SORmx/63V+bhQpOd7kHiHR868uTxe2YXZtYJYa9obPUwwEhpXwLuhhRiSObYP4svRLg4ddfJ/oKQJxwPO8OdNZAgMBAAECggEARJvT6LVkGYjCSIWUUnK8wolp0mtTQWnXtNZwvnK3yckuxuDKJaucFrh7wZPNzF+Qzq9jvtVPHGJ7dlX8dS6wsuLVTp2YeOo+NESTJX84C2MWOiHzZ2dC9zM80jOsFcERj17EeevX7kNrAcsRz2IkPTUuRyVYrDDwf4CSWhBTRQY6B2hhzJp1M4Psw02kZJCIF7x4DGXmNGPVe7AAlAPgTT45YJEdNG50HQPvkCCn/XiUPWU2SY/Uga3QG3f/DY1fEzLoAv02XToUAw0kCRgM83L/YFC4QAHtlzcwifAlLqhtQJdlE95l3M4iyEpmKtOMMMi4WazL8xnhnrFpdbZoAQKBgQDC+bZrt6dZRYY1y+GZr1+rkwE9ulwcFMEcT7jfTOCKtUjneYwEPHLFIP8x+Znpd5dIFzzdLFLre7AdOkNGAgIPIi3HxEqLNYi4kwnsVhsIZkC3fLTLBPbV5SOfs4BGIr4PvF2RFRV452GXwMa8dBCcZDEFdAeXiYsDbnGO2lDwGQKBgQCtAgRyXT+HJN5YFFM+11aDzqOnk1JxfG4wQ4XxWT4wmJYVmuroOojQD+Vmdxpg746HKVCtqwOGpuQw48t9WPhvmaJEXMNxyJPYn1TGPvCZUcW7Qyv8NKBFDv/HEp+j+vOKwNGApS21QE6aI4auodG71BSIMhkyBZGv943euOtlQQKBgQCyqcxljJcHENRY+4ixIakTOdQLUngRK1uBq/QGexXPRSuXISQSxamNxhzTGJvW+kVgRBr7uSAKtP6+6aK3xiNI5tL4mYCNa/Lq36JaNNT0r25iNR4eY2AkxKION5g1rKRpGvzKc32Hms8tFfOGwPxvssfQ7W//hoQiEtRwSNGeEQKBgBTfkG/H5llEznqsARss2ehPLxmRwbmlfJngG7HxxXoRftIefccFAOqxcEj7h7g4Tubpyi0Z2/QMAK4RXcTCzj6Qt64tzvrUa1oQZycAAwFMh2G/ifgE1W1fkOaQzaf4KBgOiXhO9YPPvWwFa0N4jJhvzwZbnQSW/WvQn1JG10fBAoGANreQEQAOrCwnD7vmTbPhvnt37QS+Ax8wlBActrFNvCNeGqExAaREFlO3El+00yIbBKDA6rwylwV1mzC7JtUbSihTEfUn9ijS4beowUSd+savPcmHiZa7mNJwPtUanjy+L8tza924y147GhVzu7qXbpWF8RL1xLN1OuKkWhGkyGQ=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://fanxiaoxiang.natapp1.cc/updateOrderStatus";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://fanxiaoxiang.natapp1.cc/orderList";

    // 签名方式
    public static String sign_type = "RSA";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";


    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}

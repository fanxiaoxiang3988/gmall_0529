package com.atguigu.gmall.manager.constant;

/**
 * Redis做缓存时使用key的一些命名规则
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/1 0001
 */
public class RedisCacheKeyConst {

    public static final String SKU_INFO_PREFIX = "sku:";
    public static final String SKU_INFO_SUFFIX = ":info";
    public static final Integer SKU_INFO_TIMEOUT = 60*60*24;//setex，以秒为单位，设置过期时间为1天
    public static final Integer LOCK_TIMEOUT = 3;//默认锁的超时时间
    public static final String LOCK_SKU_INFO = "gmall:lock:sku";//防止查询sku时出现缓存击穿的锁名称
    public static final Integer SKU_INFO_NULL_TIMEOUT= 60*5; //防止缓存穿透，数据库中没有的也存起来，存五分钟即可

    public static final String SKU_HOT_SCORE = "gmall:sku:hotscore";//商品热度在redis中保存的值

}

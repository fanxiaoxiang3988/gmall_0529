package com.atguigu.gmall.manager;

/**
 * 测试双端检索机制的单例模式
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/8/13 0013
 */
public class Singleton {

    private volatile static  Singleton singleton;

    private Singleton() {}

    public static Singleton getInstance() {
        if( singleton == null ) {
            synchronized (Singleton.class) {
                if( singleton == null ) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
        System.out.println(instance);
    }




}

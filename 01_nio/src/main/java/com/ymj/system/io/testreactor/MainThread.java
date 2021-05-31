package com.ymj.system.io.testreactor;

/**
 * @author : yemingjie
 * @date : 2021/5/29 13:46
 */
public class MainThread {
    public static void main(String[] args) {
        // 不做关于IO 和 业务的事情

        // 1.创建IO Thread（一个或者多个）
        //SelectorThreadGroup stg = new SelectorThreadGroup(1);
        SelectorThreadGroup stg = new SelectorThreadGroup(3);

        // 2.将监听的 server 注册到某一个 selector 上
        stg.bind(9999);
    }
}

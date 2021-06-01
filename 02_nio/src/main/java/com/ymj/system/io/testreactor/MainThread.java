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
        //SelectorThreadGroup stg = new SelectorThreadGroup(3);
        SelectorThreadGroup boss = new SelectorThreadGroup(3);
        SelectorThreadGroup worker = new SelectorThreadGroup(3);

        // 2.将监听的 server 注册到某一个 selector 上
        boss.setWorker(worker);

        // boss里选一个线程注册listen，触发bind，这个被选中的线程得持有workerGroup的引用
        // 因为未来listen一旦accept得到client后得去worker中next出一个线程分配
        boss.bind(9999);
    }
}

package com.ymj.system.io.testreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : yemingjie
 * @date : 2021/5/30 19:44
 */
public class SelectorThreadGroup {
    SelectorThread[] sts;
    ServerSocketChannel server = null;

    SelectorThreadGroup stg = this;

    AtomicInteger xid = new AtomicInteger(0);

    SelectorThreadGroup(int num) {
        // num 线程数
        sts = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            sts[i] = new SelectorThread();
            new Thread(sts[i]).start();
        }
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            nextSelector(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextSelector(Channel c) {

        SelectorThread st = next();
        // 1.通过队列传递数据 消息
        st.lbq.add(c);
        // 2.通过打断阻塞，让对应的线程去自己在打断后完成注册selector
        st.selector.wakeup();

       // c有可能是server 或者 client
//        ServerSocketChannel s = (ServerSocketChannel) c;
//
//        try {
//            // 功能是让selector 的selector 的select() 方法立刻返回不阻塞
//            st.selector.wakeup();
//            s.register(st.selector, SelectionKey.OP_ACCEPT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    // 无论 serverSocket 都复用这个方法
    private SelectorThread next() {
        //轮询就会很尴尬，倾斜
        int index = xid.incrementAndGet() % sts.length;
        return sts[index];

    }
}

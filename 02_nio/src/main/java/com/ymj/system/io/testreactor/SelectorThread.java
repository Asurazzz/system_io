package com.ymj.system.io.testreactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : yemingjie
 * @date : 2021/5/29 13:48
 */
public class SelectorThread implements Runnable{



    /**
     * 每个线程对应一个selector
     * 多线程情况下，该主机，该程序的并发客户端被分配到多个selector上
     * 注意每个客户端只绑定其中一个selector
     */
    Selector selector = null;

    LinkedBlockingQueue<Channel> lbq = new LinkedBlockingQueue<>();
    SelectorThreadGroup stg;

    SelectorThread(SelectorThreadGroup stg) {
        try {
            this.stg = stg;
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1.select
                //System.out.println(Thread.currentThread().getName()+"   :  before select...."+ selector.keys().size());
                int nums = selector.select();
                //System.out.println(Thread.currentThread().getName()+"   :  after select...." + selector.keys().size());
                // 2.处理selectkeys
                if (nums > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = keys.iterator();
                    // 线程处理的过程
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        // 复杂，接受客户端的过程（接收之后要注册）
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }
                // 3.处理task ： listen client
                // 队列是堆中的对象，线程的栈是独立的，堆是共享的
                if (!lbq.isEmpty()) {
                    // 只有方法的逻辑，本地变量是线程隔离的
                    Channel c = lbq.take();
                    if (c instanceof ServerSocketChannel) {
                        ServerSocketChannel server = (ServerSocketChannel) c;
                        server.register(selector, SelectionKey.OP_ACCEPT);

                        System.out.println(Thread.currentThread().getName() + " register listen");
                    } else if(c instanceof  SocketChannel) {
                        SocketChannel client = (SocketChannel) c;
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);

                        System.out.println(Thread.currentThread().getName() + " register client: " + client.getRemoteAddress());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void readHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + "read......");
        ByteBuffer buffer = (ByteBuffer)key.attachment();
        SocketChannel client = (SocketChannel)key.channel();
        buffer.clear();
        while (true) {
            try {
                int num = client.read(buffer);
                if (num > 0) {
                    // 将读到的内容翻转，然后直接写出
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if (num == 0) {
                    break;
                } else if (num < 0) {
                    // 客户端断开了
                    System.out.println("client: " + client.getRemoteAddress() + " closed......");
                    key.channel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptHandler(SelectionKey key) {
        System.out.println(Thread.currentThread().getName() + " acceptHandler...");

        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        try {
            SocketChannel client = server.accept();
            // 非阻塞
            client.configureBlocking(false);
            // stg.nextSelector
            //stg.nextSelectorV2(client);
            stg.nextSelectorV3(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWorker(SelectorThreadGroup stgWorker) {
        this.stg = stgWorker;
    }
}

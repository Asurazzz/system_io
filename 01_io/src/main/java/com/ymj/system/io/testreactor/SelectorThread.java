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

    SelectorThread() {
        try {
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
                int nums = selector.select();
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
            } catch (IOException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

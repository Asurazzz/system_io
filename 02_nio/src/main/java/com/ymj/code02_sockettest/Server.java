package com.ymj.code02_sockettest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Classname Server
 * @Description TODO
 * @Date 2021/5/31 17:06
 * @Created by yemingjie
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 1.获取通道
        ServerSocketChannel server = ServerSocketChannel.open();
        // 2.切换到非阻塞模式
        server.configureBlocking(false);
        // 3.绑定连接
        server.bind(new InetSocketAddress(8085));
        // 4.获取选择器
        Selector selector = Selector.open();
        // 5.将通道注册到选择器上，并且指定“监听接收事件”
        //读 : SelectionKey.OP_READ (1)
        //写 : SelectionKey.OP_WRITE (4)
        //连接:SelectionKey.OP_CONNECT (8)
        //接收 : SelectionKey.OP_ACCEPT (16)
        server.register(selector, SelectionKey.OP_ACCEPT);
        // 6.轮询的获取选择器上准备就绪的事件
        while (selector.select() > 0) {
            // 7.获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                // 8. 获取准备“就绪”的是事件
                SelectionKey key = iterator.next();
                // 9. 判断具体是什么事件准备就绪
                if (key.isAcceptable()) {
                    // 10.若“接收就绪”，获取客户端连接
                    SocketChannel sChannel = server.accept();
                    // 11.切换非阻塞模式
                    sChannel.configureBlocking(false);
                    // 12.将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 13.获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel)key.channel();
                    // 14.读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buffer)) > 0) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                }
                // 15.取消选择键 SelectionKey
                iterator.remove();
            }
        }
    }
}

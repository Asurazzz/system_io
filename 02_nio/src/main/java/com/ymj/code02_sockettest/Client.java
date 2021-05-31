package com.ymj.code02_sockettest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * @Classname Client
 * @Description TODO
 * @Date 2021/5/31 17:31
 * @Created by yemingjie
 */
public class Client {
    public static void main(String[] args) throws IOException {
        // 1.获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8085));
        // 2.切换到非阻塞模式
        sChannel.configureBlocking(false);
        // 3.分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 4.发送数据给服务端
        String str = "mujiutian";

        while(str != null && !"".equals(str)){
            buffer.put((new Date().toString() + "\n" + str).getBytes());
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
            str = "";
        }
        //5. 关闭通道
        sChannel.close();
    }
}

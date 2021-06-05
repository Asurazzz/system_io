package com.ymj.system.io.testrpc;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author : yemingjie
 * @date : 2021/6/5 9:55
 */
public class ClientPool {

    NioSocketChannel[] clients;
    Object[] lock;

    ClientPool(int size) {
        clients = new NioSocketChannel[size];
        lock = new Object[size];
        for (int i = 0; i < size; i++) {
            lock[i] = new Object();
        }
    }
}

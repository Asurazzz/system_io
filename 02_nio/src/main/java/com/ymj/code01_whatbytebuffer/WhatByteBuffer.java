package com.ymj.code01_whatbytebuffer;

import java.nio.ByteBuffer;

/**
 * @author : yemingjie
 * @date : 2021/5/22 17:44
 */
public class WhatByteBuffer {
    public static void main(String[] args) {
        // 堆上的
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 堆外的
        //ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        System.out.println("postition: " + buffer.position());
        System.out.println("limit : " + buffer.limit());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("mark: " + buffer);

        buffer.put("123".getBytes());

        System.out.println("==========put:123===========");
        System.out.println("mark: " + buffer);

        // 读写交替
        buffer.flip();
        System.out.println("==========flip===========");
        System.out.println("mark: " + buffer);

        buffer.get();

        System.out.println("==========get===========");
        System.out.println("mark: " + buffer);

        buffer.compact();

        System.out.println("==========compact===========");
        System.out.println("mark: " + buffer);

        buffer.clear();
        System.out.println("==========clear===========");
        System.out.println("mark: " + buffer);

    }
}

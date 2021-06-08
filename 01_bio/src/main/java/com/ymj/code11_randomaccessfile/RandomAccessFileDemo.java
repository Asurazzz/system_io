package com.ymj.code11_randomaccessfile;

import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @Classname RandomAccessFileDemo
 * @Description 注意：随机读写。数据需要规律。用RandomAccessFile类需要明确要操作的数据的位置。
 * @Date 2021/6/8 10:25
 * @Created by yemingjie
 */
public class RandomAccessFileDemo {
    public static void main(String[] args) throws Exception {
        writeFile();
        RandomAccessFile raf = new RandomAccessFile("F:\\test.txt", "r");
        raf.seek(8*1);
        byte[] buf = new byte[4];
        raf.read(buf);
        String name = new String(buf);
        System.out.println("name = " + name);
 
        int age = raf.readInt();
        System.out.println("age = " + age);
        raf.close();
    }

    public static void writeFile() throws Exception {
        //明确要操作的位置，可以多个线程操作同一份文件而不冲突。多线程下载的基本原理。
        RandomAccessFile raf = new RandomAccessFile("F:\\test.txt", "rw");

        raf.write("张三".getBytes());
        raf.writeInt(97);

        raf.write("李四".getBytes());
        // 保证字节原样性
        raf.writeInt(99);

        // 获取随机指针
        System.out.println(raf.getFilePointer());
        // 设置指针
        raf.seek(8*2);
        raf.write("王五".getBytes());
        raf.writeInt(100);
        raf.close();

    }
}

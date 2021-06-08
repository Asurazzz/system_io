package com.ymj.code10_bytearray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Classname ByteArrayStreamDemo
 * @Description
 * ByteArrayInputStream  ByteArrayOutputStream
 *
 * CharArrayReader 　　CharArrayWriter
 *
 * StringReader　　　　StringWriter
 *
 * 关闭这些流都是无效的，因为这些都未调用系统资源，不需要抛IO异常。
 * @Date 2021/6/8 9:59
 * @Created by yemingjie
 */
public class ByteArrayStreamDemo {
    static public  void main(String[] args) {
        // 源：内存
        ByteArrayInputStream bis = new ByteArrayInputStream("abcdefg".getBytes(StandardCharsets.UTF_8));
        // 目的： 内存
        //内部有个可自动增长的数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //因为都是源和目的都是内存，没有调用底层资源，所以不要关闭，即使调用了close也没有任何效果，关闭后仍然可使用，不会抛出异常。
        int ch = 0;
        while ((ch = bis.read()) != -1) {
            bos.write(ch);
        }
        System.out.println(bos.toString());
    }
}

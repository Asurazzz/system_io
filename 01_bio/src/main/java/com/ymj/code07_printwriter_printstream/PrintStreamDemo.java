package com.ymj.code07_printwriter_printstream;

import java.io.*;

/**
 * @Classname PrintStreamDemo
 * @Description 使用字节打印流复制文本文件
 * @Date 2021/6/4 11:06
 * @Created by yemingjie
 */
public class PrintStreamDemo {
    public static void main(String[] args) {
        try (BufferedReader bf = new BufferedReader(new FileReader("Copy.java"));
             PrintStream ps = new PrintStream("Copy2.java")){
            String line;
            while ((line = bf.readLine()) != null) {
                ps.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

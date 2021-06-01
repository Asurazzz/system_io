package com.ymj.code01_filereader;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * @Classname CopyFile
 * @Description 使用FileReader和FileWriter类完成文本文件复制：
 * @Date 2021/6/1 11:24
 * @Created by yemingjie
 */
public class CopyFile {
    public static void main(String[] args) {
        // 创建输入流对象
        try (FileReader reader = new FileReader("F:\\pdf\\copyfrom.txt");
             FileWriter writer = new FileWriter("F:\\pdf\\copyto.txt")){
            /**
             * 创建输出流做的工作：
             *  1.调用系统资源创建一个文件
             *  2.创建输出流对象
             *  3.把输出流对象指向文件
             */
            // 文本文件复制，一次读一个字符
            method1(reader, writer);
            // 文本文件复制，一次读一个字节数组
            method2(reader, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本文件复制，一次读一个字节数组
     * @param reader
     * @param writer
     */
    private static void method2(FileReader reader, FileWriter writer) {
        try {
            char chs[] = new char[1024];
            int len = 0;
            while ((len = reader.read(chs)) != -1) {
                writer.write(chs, 0, len);
            }
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 文本文件复制，一次读一个字符
     * @param reader
     * @param writer
     */
    private static void method1(FileReader reader, FileWriter writer) {
        int ch = 0;
        try {
            // 读数据
            while ((ch = reader.read()) != -1) {
                // 写数据
                writer.write(ch);
            }
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

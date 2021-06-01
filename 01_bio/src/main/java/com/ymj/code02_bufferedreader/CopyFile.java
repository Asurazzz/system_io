package com.ymj.code02_bufferedreader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @Classname CopyFile
 * @Description 使用BufferedReader和BufferedWriter完成文件复制
 * @Date 2021/6/1 11:45
 * @Created by yemingjie
 */
public class CopyFile {
    public static void main(String[] args) {
        try (   // 创建输入流对象
                BufferedReader br = new BufferedReader(new FileReader("F:\\pdf\\copyfrom.txt"));
                // 创建输出流对象
                BufferedWriter bw = new BufferedWriter(new FileWriter("F:\\pdf\\copyto.txt"))){
            char[] chs = new char[1024];
            int len;
            while ((len = br.read(chs)) != -1) {
                bw.write(chs, 0, len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

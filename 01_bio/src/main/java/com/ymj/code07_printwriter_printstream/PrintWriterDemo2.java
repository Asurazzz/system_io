package com.ymj.code07_printwriter_printstream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * @Classname PriteWriterDemo2
 * @Description 使用字符打印流复制文本文件
 * @Date 2021/6/4 10:52
 * @Created by yemingjie
 */
public class PrintWriterDemo2 {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("F:\\copy.txt"));
             PrintWriter pw = new PrintWriter("F:\\printcopy.txt")){
            String line;
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

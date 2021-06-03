package com.ymj.code05_inputstreamreader;

import java.io.*;

/**
 * @Classname InputStreamReaderDemo
 * @Description 使用标准输入流，读取键盘录入的数据，存储到项目根目录下的a.txt中
 *              将字节输入流转换成字符输入流，InputStreamReader
 * @Date 2021/6/3 15:02
 * @Created by yemingjie
 */
public class InputStreamReaderDemo {
    public static void main(String[] args) {
       //method1();
       //method2();
        method3();
    }

    private static void method1() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             FileWriter fw = new FileWriter("F:\\a.txt")){
            char[] chs = new char[1024];
            int len;
            while ((len = br.read(chs)) != -1) {
                fw.write(chs, 0, len);
                fw.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void method2() {
        try (Reader r = new InputStreamReader(System.in);
             FileWriter fw = new FileWriter("F:\\a.txt")){
            // 读写数据
            char[] chs = new char[1024];
            int len;
            while ((len = r.read(chs)) != -1) {
                fw.write(chs, 0, len);
                fw.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void method3() {
        try (InputStream in = System.in;
            FileWriter fw = new FileWriter("F:\\a.txt")){
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                fw.write(new String(bytes, 0, len));
                fw.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

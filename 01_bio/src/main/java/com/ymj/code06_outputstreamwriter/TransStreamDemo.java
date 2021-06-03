package com.ymj.code06_outputstreamwriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @Classname TransStreamDemo
 * @Description TODO
 * @Date 2021/6/3 15:59
 * @Created by yemingjie
 */
public class TransStreamDemo {

    public static void main(String[] args) {
        writeCN();
        readCN();
    }

    public static void readCN() {
        //InputStreamReader将字节数组使用指定的编码表解码成文字
        try (InputStreamReader isr=new InputStreamReader(new FileInputStream("F:\\a.txt"),"utf-8");) {
            char[] buff = new char[1024];
            int len = isr.read(buff);
            System.out.println(new String(buff, 0, len));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeCN() {
        //OutputStreamWriter将字符串按照指定的编码表转成字节，再使用字符流将这些字节写出去
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("F:\\a.txt"), "utf-8");){
            osw.write("您好");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}

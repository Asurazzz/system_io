package com.ymj.code04_bufferedinputstream;

import java.io.*;

/**
 * @Classname CopyImg
 * @Description 使用字节缓冲流实现图片的复制
 * @Date 2021/6/2 18:05
 * @Created by yemingjie
 */
public class CopyImg {
    public static void main(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("F:\\img.png"));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("F:\\imgcopy.png"))){
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

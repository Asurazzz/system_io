package com.ymj.code03_fileinputstream;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Classname CopyImg
 * @Description 使用字节流复制图片
 * @Date 2021/6/2 17:09
 * @Created by yemingjie
 */
public class CopyImg {
    public static void main(String[] args) {
        try(FileInputStream fin = new FileInputStream("F:\\img.png");
            FileOutputStream fot = new FileOutputStream("F:\\imgcopy.png")) {
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = fin.read(buff)) != -1) {
                fot.write(buff, 0, len);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

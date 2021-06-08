package com.ymj.code12_file;

import java.io.File;
import java.io.FileFilter;

/**
 * @Classname FileDemo3
 * @Description TODO
 * @Date 2021/6/8 10:57
 * @Created by yemingjie
 */
public class FileDemo3 {
    public static void main(String[] args) {
        File file = new File("F:\\");
        if (file.isDirectory()) {
            File[] list = file.listFiles((pathname) -> pathname.isDirectory());
//            File[] list = file.listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File pathname) {
//                    return pathname.isDirectory();
//                }
//            });
            for (File f : list) {
                System.out.println(f);
            }
        }
    }
}

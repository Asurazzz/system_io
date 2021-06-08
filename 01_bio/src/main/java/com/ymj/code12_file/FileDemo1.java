package com.ymj.code12_file;

import java.io.File;

/**
 * @Classname FileDemo1
 * @Description 打印指定文件（夹）及其所有子目录
 * @Date 2021/6/8 10:45
 * @Created by yemingjie
 */
public class FileDemo1 {
    public static void main(String[] args) {
        File file = new File("F:\\video");
        printTree(file, 0);
    }

    public static void printTree(File file, int level) {
        for (int j = 0;j < level; j++) {
            System.out.println("\t");
        }
        System.out.println(file.getAbsolutePath());
        if (file.isDirectory()) {
            level++;
            File strs[] = file.listFiles();
            for (int i = 0;i < strs.length;i++) {
                File f0 = strs[i];
                printTree(f0, level+1);
            }
        }
    }
}

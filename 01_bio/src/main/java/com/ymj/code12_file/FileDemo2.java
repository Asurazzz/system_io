package com.ymj.code12_file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Classname FileDemo2
 * @Description 过滤器
 * @Date 2021/6/8 10:54
 * @Created by yemingjie
 */
public class FileDemo2 {
    public static void main(String[] args) {
        File file = new File("F:\\");
        if (file.isDirectory()) {
            String[] list = file.list(new FilenameFilterbytxt());
            for (String l : list) {
                System.out.println(l);
            }
        }
    }

}

class FilenameFilterbytxt implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
    }
}

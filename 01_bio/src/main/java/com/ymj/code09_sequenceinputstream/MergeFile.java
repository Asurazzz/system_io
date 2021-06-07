package com.ymj.code09_sequenceinputstream;

import java.io.*;
import java.util.*;

/**
 * @Classname MergeFile
 * @Description SequenceInputStream
 *  表示其他输入流的逻辑串联。它从输入流的有序集合开始，并从第一个输入流开始读取，直到到达文件末尾，
 *  接着从第二个输入流读取，依次类推，直到到达包含的最后一个输入流的文件末尾为止。
 * @Date 2021/6/7 11:24
 * @Created by yemingjie
 */
public class MergeFile {
    public static void main(String[] args) throws Exception {
        File pathDir = new File("F:\\cutFiles");
        // 获取配置文件
        File configFile = getConfigFile(pathDir);
        // 获取配置文件信息的属性集
        Properties prop = getProperties(configFile);
        System.out.println(prop);
        //获取属性集信息，将属性集信息传递到合并方法中
        merge(pathDir, prop);
    }

    private static Properties getProperties(File configFile) {
        Properties prop = null;
        try (FileInputStream fis = new FileInputStream(configFile)){
             prop = new Properties();
            // 流中数据加载到集合中
            prop.load(fis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return prop;
    }

    private static File getConfigFile(File pathDir) {
        // 判断是否存在properties文件
        if (!pathDir.exists() && pathDir.isDirectory()) {
            throw new RuntimeException(pathDir.toString() + "不是有效目录");
        }
        File[] files = pathDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".properties");
            }
        });
        if (files.length != 1) {
            throw new RuntimeException(pathDir.toString()+"properties扩展名的文件不存在或者不唯一");
        }
        File configFile = files[0];
        return configFile;
    }

    public static void merge(File pathDir, Properties prop) throws Exception {
        String fileName = prop.getProperty("fileName");
        int partCount = Integer.valueOf(prop.getProperty("partCount"));
        List<FileInputStream> list = new ArrayList<>();
        for (int i = 0; i < partCount; i++) {
            list.add(new FileInputStream(pathDir.toString() + "\\" + i + ".part"));
        }
        //List自身无法获取Enumeration工具类，到Collection中找
        Enumeration<FileInputStream> enumeration = Collections.enumeration(list);
        SequenceInputStream sis = new SequenceInputStream(enumeration);
        FileOutputStream fos = new FileOutputStream(pathDir.toString() + "\\" + fileName);
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = sis.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.close();
        sis.close();
    }
}

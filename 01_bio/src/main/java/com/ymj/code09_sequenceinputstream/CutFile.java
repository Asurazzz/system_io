package com.ymj.code09_sequenceinputstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * @Classname CutFile
 * @Description 将一个媒体文件切割成碎片
 * 思路：1、读取源文件，将源文件的数据分别复制到多个文件
 *      2、切割方式有两种：按照碎片个数切，或者按照指定大小切
 *      3、一个输入流对应多个输出流
 *      4、每个碎片都需要编号，顺序不能错
 * @Date 2021/6/7 10:57
 * @Created by yemingjie
 */
public class CutFile {
    public static void main(String[] args) throws Exception{
        File srcFile = new File("F:\\video\\1.mp4");
        File partsDir = new File("F:\\cutFiles");
        splitFile(srcFile, partsDir);
    }

    /**
     * 切割文件
     * @param srcFile
     * @param partsDir
     */
    private static void splitFile(File srcFile, File partsDir) throws Exception{
        if (!srcFile.exists() && partsDir.isFile()) {
            throw new RuntimeException("源文件不是正确文件或者不存在");
        }
        if (!partsDir.exists()) {
            partsDir.mkdirs();
        }

        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = null;

        byte[] buf = new byte[1024*60];
        int len = 0;
        int count = 0;
        while ((len = fis.read(buf)) != -1 ) {
            // 存储碎片文件
            fos = new FileOutputStream(new File(partsDir, (count++) + ".part"));
            fos.write(buf, 0, len);
            fos.close();
        }

        /**
         * 将源文件和切割的信息也保存起来，随着碎片文件一起发送
         * 信息：源文件的名称
         * 碎片的个数
         * 将这些信息单独封装到一个文件中
         * 还要一个输出流完成此操作
         */
        String fileName = srcFile.getName();
        int partCount = count;
        fos = new FileOutputStream(new File(partsDir, count + ".properties"));
        //        fos.write(("fileName="+fileName+System.lineSeparator()).getBytes());
        //        fos.write(("fileCount="+Integer.toString(partCount)).getBytes());
        Properties prop = new Properties();
        prop.setProperty("fileName", fileName);
        prop.setProperty("partCount", Integer.toString(partCount));

        // 将属性集中的信息持久化
        prop.store(fos, "part file info");
        fis.close();
        fos.close();

    }

}

package com.ymj.code06_outputstreamwriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 利用标准输出流将文本输出到命令行
 * @Classname OutputStreamWriterDemo
 * @Description 读取项目目录下的文件copy.java,并输出到命令行
 *              由于标准输出流是字节输出流，所以只能输出字节或者字节数组，但是我们读取到的数据是字符串，如果想进行输出
 *              还需要转换成字节数组(method1)。
 *              要想通过标准输出流输出字符串，把标准输出流转换成一种字符输出流即可(method2)
 * @Date 2021/6/3 15:42
 * @Created by yemingjie
 */
public class OutputStreamWriterDemo {
    public static void main(String[] args) {
        // method1();
        //method2();
        method3();
    }

    private static void method1() {
        try  ( BufferedReader br = new BufferedReader(new FileReader("F:\\source\\system_io\\01_bio\\src\\main\\java\\com" +
                "\\ymj\\code01_filereader\\CopyFile.java"));
               BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))){
            // 用于接收读到的数据
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.write("\r\n");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void method2() {
        try(BufferedReader br = new BufferedReader(new FileReader("F:\\source\\system_io\\01_bio\\src\\main\\java\\com" +
                "\\ymj\\code01_filereader\\CopyFile.java"));
            Writer w = new OutputStreamWriter(System.out)) {
            // 用于接收数据
            String line;
            while ((line = br.readLine()) != null) {
                w.write(line);
                w.write("\r\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void method3() {
        try (BufferedReader br = new BufferedReader(new FileReader("F:\\source\\system_io\\01_bio\\src\\main\\java\\com" +
                "\\ymj\\code01_filereader\\CopyFile.java"));
             OutputStream os = System.out){
            String line;
            while ((line = br.readLine()) != null) {
                os.write(line.getBytes(StandardCharsets.UTF_8));
                os.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

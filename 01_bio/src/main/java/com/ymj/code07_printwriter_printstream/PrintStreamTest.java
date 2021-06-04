package com.ymj.code07_printwriter_printstream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 *
 * public class PrintStream extends FilterOutputStreamimplements Appendable, Closeable
 * (1)PrintStream 为其他输出流添加了功能（增加了很多打印方法），使它们能够方便地打印各种
 *      数据值表示形式(例如：希望写一个整数，到目的地整数的表现形式不变。字节流的write方法只将一个整数的最低字节写入到目的地，
 *      比如写fos.write(97)，到目的地（记事本打开文件）会变成字符'a',需要手动转换：fos.write(Integer.toString(97).getBytes());
 *      而采用PrintStream：ps.print(97)，则可以保证数据的表现形式)。
 * (2)  与其他输出流不同，PrintStream 永远不会抛出 IOException；而是，异常情况仅设置可通过 checkError 方法测试的内部标志。
 *      另外，为了自动刷新，可以创建一个 PrintStream；这意味着可在写入 byte 数组之后自动调用 flush 方法，可调用其中一个 println 方法，或写入一个换行符或字节 ('\n')。
 * (3)  PrintStream 打印的所有字符都使用平台的默认字符编码转换为字节。在需要写入字符而不是写入字节的情况下，应该使用 PrintWriter 类。
 * @Classname PrintStreamTest
 * @Description TODO
 * @Date 2021/6/4 10:57
 * @Created by yemingjie
 */
public class PrintStreamTest {
    public static void main(String[] args) {
        try (PrintStream ps = new PrintStream(new FileOutputStream("F:\\test.txt"))){
            // 输出 a
            ps.write(97);
            ps.write("a".getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

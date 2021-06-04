package com.ymj.code07_printwriter_printstream;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *
 * public class PrintWriter extends Writer
 *
 * (1)向文本输出流打印对象的格式化表示形式。此类实现在 PrintStream 中的所有 print 方法。不能输出字节，但是可以输出其他任意类型。
 *
 * (2)与 PrintStream 类不同，如果启用了自动刷新，则只有在调用 println、printf 或 format 的其中一个方法时才可能完成此操作，
 *      而不是每当正好输出换行符时才完成。这些方法使用平台自有的行分隔符概念，而不是换行符。
 *
 * (3)此类中的方法不会抛出 I/O 异常，尽管其某些构造方法可能抛出异常。客户端可能会查询调用 checkError() 是否出现错误。
 * @Classname PriteWriterDemo
 * @Description TODO
 * @Date 2021/6/4 10:44
 * @Created by yemingjie
 */
public class PrintWriterDemo {
    public static void main(String[] args) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("print.txt"), true)){
            pw.write("测试打印流");
            pw.println("此句之后换行");
            pw.println("特有功能：自动换行和自动刷新");
            pw.println("利用构造器设置自动刷新");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

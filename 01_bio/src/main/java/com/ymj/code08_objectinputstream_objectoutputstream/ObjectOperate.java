package com.ymj.code08_objectinputstream_objectoutputstream;

import java.io.*;

/**
 *
 * 使用对象输出流写对象和对象输入流读对象
 * 注意：如果Student没有序列化，会抛出java.io.NotSerializableException
 * Serializable：序列号，是一个标识接口，只起标识作用，没有方法
 * 当一个类的对象需要IO流进行读写的时候，这个类必须实现接口
 * @Classname ObjectOperate
 * @Description TODO
 * @Date 2021/6/4 11:36
 * @Created by yemingjie
 */
public class ObjectOperate {

    public static void main(String[] args) {
        writeObject();
        // 创建对象输入流的对象
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("a.txt"))) {
            Object obj;
            // 写对象的最后加null,然后再判断就不会报错
            while ((obj = ois.readObject()) != null) {
                System.out.println(obj);
            }
//            while (true) {
//                Object obj = ois.readObject();
//                System.out.println(obj);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("读到了文件末尾");
        }
    }

    public static void writeObject() {
        try (FileOutputStream fos = new FileOutputStream("a.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            // 创建学生对象
            Student s1 = new Student("张三", 11);
            Student s2 = new Student("李四", 12);
            Student s3 = new Student("王五", 13);

            // 写出学生对象
            oos.writeObject(s1);
            oos.writeObject(s2);
            oos.writeObject(s3);
            oos.writeObject(null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

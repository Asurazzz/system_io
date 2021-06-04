package com.ymj.code08_objectinputstream_objectoutputstream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname ObjectOperate2
 * @Description 使用对象输出流写对象和对象输入流读对象,
 *              解决读取对象出现异常的问题,使用集合类
 * @Date 2021/6/4 11:49
 * @Created by yemingjie
 */
public class ObjectOperate2 {

    public static void main(String[] args){
        listMethod();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("b.txt"))){
            // 读取数据
            Object obj = ois.readObject();
            //System.out.println(obj);
            //向下转型
            ArrayList<Student> list = (ArrayList<Student>) obj;
            for (Student s : list) {
                System.out.println(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void listMethod() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("b.txt"))){
            List<Student> list = new ArrayList<>();
            // 添加学生对象
            list.add(new Student("zhangsan", 20));
            list.add(new Student("lisi", 30));
            // 写出集合对象
            oos.writeObject(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package com.ymj.code08_objectinputstream_objectoutputstream;

import java.io.Serializable;

/**
 * @Classname Student
 * @Description TODO
 * @Date 2021/6/4 11:14
 * @Created by yemingjie
 */
public class Student implements Serializable {
    private static final long serialVersionUID = -8942780382144699003L;
    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

package com.ymj.system.io.testrpc;

/**
 * @author : yemingjie
 * @date : 2021/6/6 10:44
 */
public class Packmsg {
    MyContent content;
    MyHeader header;

    public Packmsg(MyHeader header, MyContent content) {
        this.header = header;
        this.content = content;
    }

    public MyContent getContent() {
        return content;
    }

    public void setContent(MyContent content) {
        this.content = content;
    }

    public MyHeader getHeader() {
        return header;
    }

    public void setHeader(MyHeader header) {
        this.header = header;
    }
}

package com.ymj.system.io.testrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author : yemingjie
 * @date : 2021/6/6 11:19
 */
public class SerDerUtil {
    static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public synchronized static byte[] ser(Object msg) {
        out.reset();
        ObjectOutputStream oot = null;
        byte[] msgBody = null;
        try {
            oot = new ObjectOutputStream(out);
            oot.writeObject(msg);
            msgBody = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgBody;
    }
}

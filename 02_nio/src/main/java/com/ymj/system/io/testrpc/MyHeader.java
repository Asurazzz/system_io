package com.ymj.system.io.testrpc;

import java.io.Serializable;

/**
 * @author : yemingjie
 * @date : 2021/6/5 9:27
 * 通信上协议
 */
public class MyHeader implements Serializable {

    /**
     * 1.ooxx值
     * 2.UUID：requestID
     * 3.DATA_LEN
     */
    int flag;
    long requestID;
    long dataLen;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }
}

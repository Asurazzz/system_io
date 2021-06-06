package com.ymj.system.io.testrpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * 解码器
 * @author : yemingjie
 * @date : 2021/6/6 10:37
 */
public class ServerDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("channel start..." + byteBuf.readableBytes());
        while (byteBuf.readableBytes() >= 104) {
            byte[] bytes = new byte[104];
            //byteBuf.readBytes(bytes);
            // 从哪里读取，读多少，但是readindex不变
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream out = new ObjectInputStream(in);
            MyHeader header = (MyHeader) out.readObject();
            System.out.println("server response @ id :" + header.getRequestID());

            if (byteBuf.readableBytes() >= header.getDataLen()) {
                // 处理指针，移动指针到body开始的位置
                byteBuf.readBytes(104);
                byte[] data = new byte[(int) header.getDataLen()];
                byteBuf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);
                MyContent content = (MyContent) doin.readObject();
                System.out.println("server => content:" + content.getName());
                list.add(new Packmsg(header, content));
            } else {
                // 字数不够的时候打印还剩余多少
                // 进入这一步说明数据被截断了
                System.out.println("channel else:" + byteBuf.readableBytes());
                break;
            }

        }
    }
}

package com.ymj.system.io.testrpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author : yemingjie
 * @date : 2021/6/5 11:10
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    /**
     * provider
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        ByteBuf sendBuf = byteBuf.copy();

        if (byteBuf.readableBytes() >= 104) {
            byte[] bytes = new byte[104];
            byteBuf.readBytes(bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream out = new ObjectInputStream(in);
            MyHeader header = (MyHeader) out.readObject();
            System.out.println("server response @ id :" + header.getRequestID());

            if (byteBuf.readableBytes() >= header.getDataLen()) {
                byte[] data = new byte[(int) header.getDataLen()];
                byteBuf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(din);
                MyContent content = (MyContent) doin.readObject();
                System.out.println("server => content:" + content.getName());
            }

        }
        ChannelFuture channelFuture = ctx.writeAndFlush(sendBuf);
        channelFuture.sync();
    }
}

package com.ymj.system.io.testrpc;

import com.ymj.code02_sockettest.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


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
//        ByteBuf byteBuf = (ByteBuf) msg;
//        ByteBuf sendBuf = byteBuf.copy();
//
//
//        ChannelFuture channelFuture = ctx.writeAndFlush(sendBuf);
//        channelFuture.sync();

        Packmsg requestPkg = (Packmsg) msg;
        System.out.println("server handler: " + requestPkg.content.getArgs()[0]);

        // 处理完成之后，给客户端返回
        // 要注意bytebuf
        // requestID
        // 来的时候flag为0x14141414


        String ioThreadName = Thread.currentThread().getName();
        // 1.直接在当前方法处理IO和业务
        // 2.使用netty自己的eventLoop处理业务
        ctx.executor().execute(() -> {
            String execThreadName = Thread.currentThread().getName();
            MyContent content = new MyContent();
            String s = "io Thread: " + ioThreadName + "exec thread: " + execThreadName + "from args:" + requestPkg.content.getArgs()[0];
            System.out.println(s);
            content.setRes(s);

            byte[] contentByte = SerDerUtil.ser(content);

            MyHeader resHeader = new MyHeader();
            resHeader.setRequestID(requestPkg.header.getRequestID());
            resHeader.setFlag(0x14141424);
            resHeader.setDataLen(contentByte.length);
            byte[] headerByte = SerDerUtil.ser(resHeader);
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerByte.length + contentByte.length);

            byteBuf.writeBytes(headerByte);
            byteBuf.writeBytes(contentByte);
            //ctx.writeAndFlush(byteBuf);

        });

    }
}

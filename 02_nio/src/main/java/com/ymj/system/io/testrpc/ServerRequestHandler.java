package com.ymj.system.io.testrpc;

import io.netty.buffer.ByteBuf;
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

        Packmsg buf = (Packmsg) msg;
        System.out.println("server handler: " + buf.content.getArgs()[0]);
    }
}

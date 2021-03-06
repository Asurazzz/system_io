package com.ymj.system.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author : yemingjie
 * @date : 2021/6/1 21:48
 */
public class MyNetty {

    @Test
    public void myByteBuf() {
        // 超出最大20就会报错
        //ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(8, 20);
        //ByteBuf buf = UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);
        print(buf);

        buf.writeBytes(new byte[]{1,2,3,4});
        print(buf);
        buf.writeBytes(new byte[]{1,2,3,4});
        print(buf);
        buf.writeBytes(new byte[]{1,2,3,4});
        print(buf);
        buf.writeBytes(new byte[]{1,2,3,4});
        print(buf);
        buf.writeBytes(new byte[]{1,2,3,4});
        print(buf);
//        buf.writeBytes(new byte[]{1,2,3,4});
//        print(buf);
    }


    public static void print(ByteBuf buf) {
        // 是否可读
        System.out.println("buf.isReadable(): " + buf.isReadable());
        // buffer数组中从哪可以读
        System.out.println("buf.readerIndex():  "+ buf.readerIndex());
        // 可以读多少
        System.out.println("buf.readableBytes():    " + buf.readableBytes());
        // 是否可写
        System.out.println("buf.isWritable()    " + buf.isWritable());
        // 从哪开始写
        System.out.println("buf.writerIndex()   " + buf.writerIndex());
        // 还可以写的字节数，和初始容量相关
        System.out.println("buf.writableBytes()     " + buf.writableBytes());
        // 上限
        System.out.println("buf.capacity()  " + buf.capacity());
        // 最大上限
        System.out.println("buf.maxCapacity()   " + buf.maxCapacity());
        // false 堆内还是   true 堆外
        System.out.println("buf.isDirect()  " + buf.isDirect());
        System.out.println("=================================");
    }


    @Test
    public void loopExecutor() throws Exception {
        NioEventLoopGroup selector = new NioEventLoopGroup(1);
        selector.execute(() -> {
            try {
                for (;;) {
                    System.out.println("hello World001");
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        selector.execute(() -> {
            try {
                for (;;) {
                    System.out.println("hello World002");
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        System.in.read();
    }


    /**
     * 客户端  使用nc -l 192.168.195.132 9090 连接
     * @throws Exception
     */
    @Test
    public void clientModel() throws Exception{
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        thread.register(client);


        // 响应式：可以打印出从服务端打出的信息
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new MyInHandler());

        // reactor 异步
        ChannelFuture connect = client.connect(new InetSocketAddress("192.168.195.132", 9090));
        ChannelFuture sync = connect.sync();

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello server....".getBytes(StandardCharsets.UTF_8));
        ChannelFuture send = client.writeAndFlush(byteBuf);
        send.sync();

        sync.channel().closeFuture().sync();
        System.out.println("client over...");
    }


    /**
     * 可以使用windows 的telnet 127.0.0.1 9090 来测试
     * @throws Exception
     */
    @Test
    public void serverModel() throws Exception{
        NioEventLoopGroup thread = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();
        // 注册
        thread.register(server);

        ChannelPipeline pipeline = server.pipeline();
        // accept接收客户端，并且注册到selector
        //pipeline.addLast(new MyAcceptHandler(thread, new MyInHandler()));
        pipeline.addLast(new MyAcceptHandler(thread, new ChannelInit()));

        ChannelFuture bind = server.bind(new InetSocketAddress("127.0.0.1", 9090));

        bind.sync().channel().closeFuture().sync();
        System.out.println("server close...");
    }


    /**
     * netty客户端
     * @throws Exception
     */
    @Test
    public void nettyClient() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();

        ChannelFuture connect = bs.group(group)
                .channel(NioSocketChannel.class)
  //                        .handler(new ChannelInit())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new MyInHandler());
                    }
                })
                .connect(new InetSocketAddress("192.168.195.132", 9090));
        Channel client = connect.sync().channel();

        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes(StandardCharsets.UTF_8));
        ChannelFuture send = client.writeAndFlush(buf);
        send.sync();

        client.closeFuture().sync();
    }


    @Test
    public void nettyServer() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap bs = new ServerBootstrap();

        ChannelFuture bind = bs.group(group, group)
                .channel(NioServerSocketChannel.class)
                // .childHandler(new MyInHandler());
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MyInHandler());
                    }
                })
                .bind(new InetSocketAddress("127.0.0.1", 9999));

        bind.sync().channel().closeFuture().sync();
    }


}

class MyAcceptHandler extends ChannelInboundHandlerAdapter{
    private final EventLoopGroup selector;
    private final ChannelHandler handler;

    public MyAcceptHandler(EventLoopGroup thread, ChannelHandler myInHandler) {
        this.selector = thread;
        this.handler = myInHandler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server register......");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocketChannel client = (SocketChannel) msg;

        // 2. 响应式
        ChannelPipeline pipeline = client.pipeline();
        // client:pipeline[ChannelInit]
        pipeline.addLast(handler);

        // 1.注册
        selector.register(client);

    }
}

/**
 * 为啥要有一个initHandler
 *  因为不设计一个来调用MyInHandler而是直接调用MyInHandler的话，
 *  MyInhandler就会变成共享的单例
 */
@ChannelHandler.Sharable
class ChannelInit extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        ChannelPipeline p = client.pipeline();
        // 2.clinet::pipeline[ChannelInit,MyInHandler]
        p.addLast(new MyInHandler());
        ctx.pipeline().remove(this);
    }
}



// 添加注解可以多个客户端共享
//@ChannelHandler.Sharable
class MyInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client register......");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client active......");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        // 会移动指针
        //CharSequence charSequence = buffer.readCharSequence(buffer.readableBytes(), CharsetUtil.UTF_8);
        // 不会移动指针
        CharSequence charSequence = buffer.getCharSequence(0, buffer.readableBytes(), CharsetUtil.UTF_8);
        // 打印在本地
        System.out.println(charSequence);
        // 给别人打印回去
        ctx.writeAndFlush(buffer);
    }
}

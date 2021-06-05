package com.ymj.system.io.testrpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author : yemingjie
 * @date : 2021/6/5 9:08
 */
public class MyRPCTest {


    @Test
    public void startServer() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = boss;

        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(worker, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("server accept client port:" + ch.remoteAddress().getPort());
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ServerRequestHandler());
                    }
                }).bind(new InetSocketAddress("localhost", 9090));

        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * 模拟consumer端
     */
    @Test
    public void get() {

        new Thread(() -> {
            startServer();
        }).start();

        System.out.println("server started......");

        Car car = proxyGet(Car.class);
        car.run("hello");

//        Fly fly = proxyGet(Fly.class);
//        fly.fly("hello");
    }

    public static <T>T proxyGet(Class<T> interfaceInfo) {

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] methodInfo = {interfaceInfo};

        return (T) Proxy.newProxyInstance(classLoader, methodInfo, (proxy, method, args) -> {
            // 1.调用服务，方法，参数 ===> 封装成message
            String name = interfaceInfo.getName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            MyContent content = new MyContent();
            content.setName(name);
            content.setMethodName(methodName);
            content.setParameterTypes(parameterTypes);
            content.setArgs(args);

            // 使用IO
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oot = new ObjectOutputStream(out);
            oot.writeObject(content);
            byte[] msgBody = out.toByteArray();

            // 2. requestID + message 本地要缓存
            // 协议 【header<>】【msgBody】
            MyHeader header = createHeader(msgBody);

            // 重置
            out.reset();
            oot = new ObjectOutputStream(out);
            oot.writeObject(header);
            byte[] msgHeader = out.toByteArray();

            // 3.连接池：获取连接
            ClientFacotry factory = ClientFacotry.getFactory();
            NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));

            // 4.发送：走IO
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);

            CountDownLatch countDownLatch = new CountDownLatch(1);
            long id = header.requestID;
            ResponseHandler.addCallBack(id, () -> {
                countDownLatch.countDown();
            });

            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
            // io是双向的，看似有个sync，仅代表out
            channelFuture.sync();

            countDownLatch.await();
            return null;
        });
    }

    public static MyHeader createHeader(byte[] msg) {
        MyHeader header = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        header.setFlag(f);
        header.setDataLen(size);
        header.setRequestID(requestID);
        return header;
    }


}

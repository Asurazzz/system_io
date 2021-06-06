package com.ymj.system.io.testrpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : yemingjie
 * @date : 2021/6/5 9:56
 */
public class ClientFacotry {
    // 一个consumer可以连接很多的provider
    int poolSize = 1;
    private static final ClientFacotry factory;

    Random random = new Random();
    NioEventLoopGroup clientWorker;


    private ClientFacotry(){}
    static {
        factory = new ClientFacotry();
    }
    public static ClientFacotry getFactory() {
        return factory;
    }

    // 一个consumer可以连接多个provider，每一个都有自己的pool
    ConcurrentHashMap<InetSocketAddress, ClientPool> outboxs = new ConcurrentHashMap<>();

    public synchronized NioSocketChannel getClient(InetSocketAddress address) {
        ClientPool clientPool = outboxs.get(address);
        if (clientPool == null) {
            outboxs.putIfAbsent(address, new ClientPool(poolSize));
            clientPool = outboxs.get(address);
        }
        int i = random.nextInt(poolSize);
        if (clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
            return clientPool.clients[i];
        }
        synchronized (clientPool.lock[i]) {
            return clientPool.clients[i] = create(address);
        }

    }
    private NioSocketChannel create(InetSocketAddress address) {
        // 基于netty客户端创建方式
        clientWorker = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientResponses());
                    }
                }).connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel)connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }
}
class ClientResponses extends ChannelInboundHandlerAdapter {
    /**
     * consumer
     * @param ctx
     * @param msg
     * @throws Exception
     */


    public void channelRead1(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packmsg responseMsg = (Packmsg) msg;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() >= 104) {
            byte[] bytes = new byte[104];
            byteBuf.readBytes(bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream out = new ObjectInputStream(in);
            MyHeader header = (MyHeader) out.readObject();
            System.out.println("client response @ id :" + header.getRequestID());

            // TODO
            //ResponseHandler.runCallBack(header.requestID);

//            if (byteBuf.readableBytes() >= header.getDataLen()) {
//                byte[] data = new byte[(int) header.getDataLen()];
//                byteBuf.readBytes(data);
//                ByteArrayInputStream din = new ByteArrayInputStream(data);
//                ObjectInputStream doin = new ObjectInputStream(din);
//                MyContent content = (MyContent) doin.readObject();
//                System.out.println(content.getName());
//            }

        }
    }
}

 class ResponseHandler {
    static ConcurrentHashMap<Long, Runnable> mapping = new ConcurrentHashMap<>();

    public static void addCallBack(long requestID, Runnable cb) {
        mapping.putIfAbsent(requestID, cb);
    }
    public static void runCallBack(long requestID) {
        Runnable runnable = mapping.get(requestID);
        runnable.run();
        removeCB(requestID);
    }

     private static void removeCB(long requestID) {
         mapping.remove(requestID);
     }
 }

package com.zhangjr.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty服务端
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup和WorkerGroup

        //1.创建两个线程组，bossGroup和workerGroup
        //2.bossGroup只是处理连接请求，真正的客户端业务处理，会交给workerGroup完成
        //3.两个都是无限循环
        //4.bossGroup和workerGroup，默认有核心数*2的线程数
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            //设置两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    //使用NioSoketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    //使用线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //TODO 这个handler对应的是bossGroup。childHandler对应workerGroup
                    //.handler(null)
                    //给我们的workerGroup的EventLoop对应的管道设置处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //创建一个通道初始化对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //设置处理器。实际处理数据
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务器 is ready...");

            //绑定一个端口，并且同步。生成了一个ChannelFuture对象
            //启动服务器并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf诸恶监听器，监听我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口6668,成功！");
                    } else {
                        System.out.println("监听端口6668,失败...");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

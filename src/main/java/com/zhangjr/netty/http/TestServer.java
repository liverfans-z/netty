package com.zhangjr.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实例要求：
 * Netty 服务器在 6668 端口监听，浏览器发出请求 "http://localhost:6668/ "
 * 服务器可以回复消息给客户端 "Hello! 我是服务器 5 " ,  并对特定请求资源进行过滤.
 * <p>
 * 目的：Netty 可以做Http服务开发，并且理解Handler实例和客户端及其请求的关系.
 */
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //加入自己写的处理类
                    .childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

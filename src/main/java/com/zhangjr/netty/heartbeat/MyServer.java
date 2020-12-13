package com.zhangjr.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检测机制
 * <p>
 * 编写一个 Netty心跳检测机制案例, 当服务器超过3秒没有读时，就提示读空闲
 * <p>
 * 当服务器超过5秒没有写操作时，就提示写空闲
 * 实现当服务器超过7秒没有读或者写操作时，就提示读写空闲
 *
 * @author ZhangJR
 */
public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //bossGroup增加日志处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();

                            //加入netty提供的IdleStateHandler
                            /**
                             * 1.IdleStateHandler是netty提供的处理空闲状态的处理器
                             * 2.参数
                             *      long readerIdleTime：表示多长时间没有读到数据了，就会发送心跳检测包，检测是否还是连接的状态
                             *      long writerIdleTime：表示多长时间没有写操作了，也会发送心跳检测包，检测是否还是连接的状态
                             *      long allIdleTime：   表示多长时间既没有读也没有写了，也会发送心跳检测包，检测是否还是连接的状态
                             *
                             *
                             * 3.文档说明
                             * Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             * read, write, or both operation for a while.
                             *
                             * 4.当IdleStateEvent触发后，就会传递给管道的下一个handler处理
                             * 通过调用(触发)下一个handler的userEventTriggered，在该方法中处理
                             *
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            System.out.println("netty 心跳服务器启动...");
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

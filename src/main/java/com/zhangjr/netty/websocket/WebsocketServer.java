package com.zhangjr.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Netty实现长连接
 *
 * <p>
 * 要求：实现基于webSocket的长连接的全双工的交互
 * 改变Http协议多次请求的约束，实现长连接了， 服务器可以发送消息给浏览器
 * 客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知
 *
 * @author ZhangJR
 */
public class WebsocketServer {

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

                            //因为基于http协议，所以需要使用http的编、解码器
                            pipeline.addLast(new HttpServerCodec());

                            //是以[块方式]写，添加ChunkedWrite处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            1.因为http的数据在传输过程中是分段的，HttpObjectAggregator就是可以将多个段，聚合起来
                            2.这就是为什么，当浏览器发送大量数据，就会发送多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                            1.对于websocket，它的数据是以 帧(frame) 的形式传递的
                            2.可以看到，WebSocketFrame下面有6个子类
                            3.浏览器请求，uri：ws://localhost:7000/xxxxx，其中xxxxx与这里的websocketPath需要对应
                            4.WebSocketServerProtocolHandler核心功能：将http协议升级为 ws 协议，保持长连接
                                4.1是通过状态码101，来转换
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义handler，处理业务逻辑
                            pipeline.addLast(new TextWebSocketFrameHandler());
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

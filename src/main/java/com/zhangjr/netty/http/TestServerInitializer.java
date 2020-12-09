package com.zhangjr.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpServerCodec  code c=> [coder - decoder]
        //1.HttpServerCodec是netty提供的http的编解码器
        pipeline.addLast("MyGoydai", new HttpServerCodec());

        //增加一个自定义的Handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}

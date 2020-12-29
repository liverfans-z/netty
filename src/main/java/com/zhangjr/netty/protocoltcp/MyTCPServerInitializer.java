package com.zhangjr.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:自定义协议解决TCP粘包拆包的问题
 * @author: ZhangJR
 * @create: 2020/12/17 21:17
 */
public class MyTCPServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //TODO 这里顺序也需要注意。入站是从上往下执行，比如这里是先执行编码，再进入handler
        //加入解码器
        pipeline.addLast(new MyMessageDecoder());

        //TODO 出站是从下往上执行
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyTCPServerHandler());
    }
}

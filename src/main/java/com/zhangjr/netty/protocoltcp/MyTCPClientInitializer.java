package com.zhangjr.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:自定义协议解决TCP粘包拆包的问题
 * @author: ZhangJR
 * @create: 2020/12/15 21:02
 */
public class MyTCPClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //TODO 这里写的顺序，是有影响的，写反了不会触发
        //TODO 出站是从下往上执行，先执行handler，再执行编码
        //设置编码器
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyMessageDecoder());
        //设置处理器
        pipeline.addLast(new MyTCPClientHandler());
    }
}

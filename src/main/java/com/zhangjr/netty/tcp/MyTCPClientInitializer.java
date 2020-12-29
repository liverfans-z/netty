package com.zhangjr.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:
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

        pipeline.addLast(new MyTCPClientHandler());
    }
}

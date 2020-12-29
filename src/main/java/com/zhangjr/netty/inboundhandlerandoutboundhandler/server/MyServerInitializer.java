package com.zhangjr.netty.inboundhandlerandoutboundhandler.server;

import com.zhangjr.netty.inboundhandlerandoutboundhandler.client.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 20:51
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //入站的handler，进行解码 MyByteToLongDecoder
//        pipeline.addLast(new MyByteToLongDecoder());

        //ReplayingDecoder解码器
        pipeline.addLast(new MyByteToLongDecoder2());

        //出站handler，给客户端返回数据，会走这个encoder。它和上面的decoder互不干扰
        pipeline.addLast(new MyLongToByteEncoder());

        //加入自定义handler，处理业务逻辑
        pipeline.addLast(new MyServerHandler());
    }

}

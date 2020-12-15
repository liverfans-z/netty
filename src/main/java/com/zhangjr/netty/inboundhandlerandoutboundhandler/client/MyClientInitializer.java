package com.zhangjr.netty.inboundhandlerandoutboundhandler.client;

import com.zhangjr.netty.inboundhandlerandoutboundhandler.server.MyByteToLongDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 21:02
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 标注一下，从客户端往服务端发数据，对客户端来说，是数据的出站，所以pipeline会出尾 → 头执行
     * 服务端接收数据时，对服务端来说，是数据的入站，所以会从pipeline的头，到尾执行
     *
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个出站的handler，对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());

        //入站解码器(入站handler)
        pipeline.addLast(new MyByteToLongDecoder());

        //加入一个自定义的handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}

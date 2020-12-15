package com.zhangjr.netty.inboundhandlerandoutboundhandler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 20:56
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到来自客户端数据:" + ctx.channel().remoteAddress() + ",读取到:" + msg);

        //给客户端回送一个null
        ctx.writeAndFlush(98765L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

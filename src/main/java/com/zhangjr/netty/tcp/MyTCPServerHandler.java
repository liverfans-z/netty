package com.zhangjr.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/17 21:18
 */
public class MyTCPServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];

        msg.readBytes(buffer);

        //转字符串
        String message = new String(buffer, CharsetUtil.UTF_8);

        System.out.println("服务器收到消息:" + message);
        System.out.println("服务器收到数据量:" + (++this.count));

        //回送数据
        ByteBuf byteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.toString());
        ctx.close();
    }
}

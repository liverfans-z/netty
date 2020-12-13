package com.zhangjr.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时，就会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送一个Student 对象到服务器
        StudentPOJO.Student student = StudentPOJO.Student
                .newBuilder()
                .setId(4)
                .setName("花和尚鲁智深")
                .build();

        ctx.writeAndFlush(student);
    }

    /**
     * 当通道有读取事件时，会触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("服务器回复:" + buf.toString(StandardCharsets.UTF_8));
        System.out.println("服务器地址:" + ctx.channel().remoteAddress());
    }

    /**
     * 发生异常时
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

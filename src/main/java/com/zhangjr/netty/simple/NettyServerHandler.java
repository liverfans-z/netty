package com.zhangjr.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * 自定义Handler，需要继承Netty规定好的某个handler适配器
 * 这时，我们自定义的handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 数据读取实际，(这里我们可以读取客户端发送的消息)
     *
     * @param ctx 上下文对象，含义管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据(默认Object)
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx:" + ctx);

        //将msg转换成一个ByteBuf
        //ByteBuf是Netty提供的，不是NIO的。
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送消息是:" + buf.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }

    /**
     * 数据处理完毕，给客户端回复
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端触发回复消息...");
        //write  flush  将数据写入到缓冲，并刷新
        //对发送的数据进行编码，
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 O(∩_∩)O哈哈~", StandardCharsets.UTF_8));
    }

    /**
     * 处理异常，一般需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}

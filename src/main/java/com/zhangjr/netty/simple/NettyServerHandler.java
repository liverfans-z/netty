package com.zhangjr.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Handler，需要继承Netty规定好的某个handler适配器
 * 这时，我们自定义的handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 数据读取实际，(这里我们可以读取客户端发送的消息)
     * <p>
     * channel和pipeline属于你中有我，我中有你。
     * ctx包含channel和pipeline，里面有很多很多的信息
     *
     * @param ctx 上下文对象，含义管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据(默认Object)
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //比如这里有一个非常耗费时间的业务 异步执行--->提交该channel对应的NIOEventLoop的 **taskQueue** 中

        //解决方案1：用户程序自定义的普通任务。
//        ctx.channel().eventLoop().execute(() -> {
//            //TODO 这里注意，如果写了多个execute，但是在taskQueue中，其实就是一个线程，有个10秒+20秒=30秒的问题
//            //实现效果：先打印go on，然后返回客户端喵1,10s后返回客户端喵2
//            try {
//                Thread.sleep(10 * 1000);
//            } catch (InterruptedException e) {
//                System.err.println("发生异常:" + e.toString());
//            }
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 O(∩_∩)O哈哈2222~", StandardCharsets.UTF_8));
//        });
//        System.out.println("go on...");

        //解决方案2：用户自定义定时任务
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                System.err.println("发生异常:" + e.toString());
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 O(∩_∩)O哈哈44~", StandardCharsets.UTF_8));
        }, 10, TimeUnit.SECONDS);
        System.out.println("go on...");


//        System.out.println("server ctx:" + ctx);
//        //将msg转换成一个ByteBuf
//        //ByteBuf是Netty提供的，不是NIO的。
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是:" + buf.toString(StandardCharsets.UTF_8));
//        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }

    /**
     * 数据处理完毕，给客户端回复
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //write  flush  将数据写入到缓冲，并刷新
        //对发送的数据进行编码，
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 O(∩_∩)O哈哈1111~", StandardCharsets.UTF_8));
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

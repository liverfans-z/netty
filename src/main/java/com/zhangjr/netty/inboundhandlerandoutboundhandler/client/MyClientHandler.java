package com.zhangjr.netty.inboundhandlerandoutboundhandler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 21:07
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        System.out.println("收到来自服务端数据:" + ctx.channel().remoteAddress() + ",读取到:" + msg);
    }

    /**
     * 发送数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler,发送数据...");

        //发送long
        ctx.writeAndFlush(123456L);

        /*
        分析：
        1."abcdabcdabcdabcd" 是16个字节
        2.该处理器的前一个handler是 MyLongToByteEncoder
        3.MyLongToByteEncoder 父类是 MessageToByteEncoder，父类会去判断数据是不是应该处理的类型，不是的话不会处理
        所以这里写非long类型的话，MyLongToByteEncoder不会走
        4.因此，我们要注意，编写Encoder要注意传入的数据类型与处理的数据类型要一致
         */
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}

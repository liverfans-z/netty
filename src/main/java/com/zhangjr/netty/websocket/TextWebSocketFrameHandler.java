package com.zhangjr.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @description: Websocket消息处理类
 * 泛型TextWebSocketFrame，表示一个文本帧(frame)
 * @author: ZhangJR
 * @create: 2020/12/13 11:14
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息:" + msg.text());

        //回复消息
        ctx.writeAndFlush(new TextWebSocketFrame("回复,服务器时间:" + LocalDateTime.now() + msg.text()));
        System.out.println("回复消息完成...");
    }

    /**
     * 当web客户端连接后，就会触发这个方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        /*
        id表示一个唯一的值，有longText和shortText两种形式，
        LongText是惟一的
        ShortText不是唯一的
         */
        System.out.println("handlerAdded被调用long:" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用short:" + ctx.channel().id().asShortText());
    }

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        System.out.println("handlerRemoved被调用:" + ctx.channel().id().asLongText());
    }

    /**
     * 异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生:" + cause.toString());
        //关闭连接
        ctx.close();
    }
}

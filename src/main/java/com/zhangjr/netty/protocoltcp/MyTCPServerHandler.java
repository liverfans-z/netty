package com.zhangjr.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @description:自定义协议解决TCP粘包拆包的问题
 * @author: ZhangJR
 * @create: 2020/12/17 21:18
 */
public class MyTCPServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //读取到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("服务端接收到数据如下:");
        System.out.println("服务端拿到长度=" + len);
        System.out.println("服务端拿到内容=" + new String(content, StandardCharsets.UTF_8));

        System.out.println("服务器接收到协议包数量:" + (++this.count));

        //回复消息
        String responseMsg = UUID.randomUUID().toString();
        byte[] responseMsgContent = responseMsg.getBytes(StandardCharsets.UTF_8);
        int responseMsgLength = responseMsgContent.length;
        //构建协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(responseMsgContent);
        messageProtocol.setLen(responseMsgLength);

        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端异常:" + cause.toString());
        ctx.close();
    }
}

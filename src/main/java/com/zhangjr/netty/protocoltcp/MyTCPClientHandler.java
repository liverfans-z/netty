package com.zhangjr.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * @description:自定义协议解决TCP粘包拆包的问题
 * @author: ZhangJR
 * @create: 2020/12/17 21:13
 */
public class MyTCPClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("客户端接收到消息如下:");
        System.out.println("客户端收到长度=" + len);
        System.out.println("客户端收到内容=" + new String(content, StandardCharsets.UTF_8));

        System.out.println("客户端接收到协议包数量:" + (++this.count));
    }

    /**
     * 使用客户端发送10条数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("触发1");
        //使用客户端发送10条数据“今天天气冷，吃火锅”
        for (int i = 0; i < 5; i++) {
            String msg = "今天天气冷，吃火锅" + i;
            byte[] conntent = msg.getBytes(StandardCharsets.UTF_8);
            int length = conntent.length;

            //创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setContent(conntent);
            messageProtocol.setLen(length);

            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("异常:" + cause.toString());
        ctx.close();
    }
}

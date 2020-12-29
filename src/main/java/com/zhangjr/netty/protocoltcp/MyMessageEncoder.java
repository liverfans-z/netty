package com.zhangjr.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: MessageProtocol对应的编码器
 * @author: ZhangJR
 * @create: 2020/12/29 20:55
 */
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMessageEncoder.encode被调用");

        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}

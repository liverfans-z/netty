package com.zhangjr.netty.inboundhandlerandoutboundhandler.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 21:04
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {

    /**
     * 编码方法，对出站数据编码
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder,encode被调用,msg=" + msg);

        out.writeLong(msg);
    }
}

package com.zhangjr.netty.inboundhandlerandoutboundhandler.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2020/12/15 20:52
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * decode会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list，
     * 或者是，ByteBuf没有更多的可读字节为止
     * <p>
     * 如果list out 不为空，就会将list的内容传递给下一个ChannelInBoundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文
     * @param in  入站的BuyeBuf
     * @param out List集合，将解码后的数据传给下一个 handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder,decode被调用");
        //因为 long 是8个字节，需要判断有8个字节，才能读取一个long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }

}

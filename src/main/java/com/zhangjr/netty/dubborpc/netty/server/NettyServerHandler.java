package com.zhangjr.netty.dubborpc.netty.server;

import com.zhangjr.netty.dubborpc.RPCConstant;
import com.zhangjr.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description: 服务器端逻辑处理类，服务器端比较简单
 * @author: ZhangJR
 * @create: 2021/01/20 20:14
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg=" + msg);

        //客户端在调用服务器api时，我们需要定义一个协议
        //比如我们要求每次发消息时，都必须以某个字符串开头。“zhangjr#api#xxxxxxxx”
        String dataStr = msg.toString();
        if (dataStr.startsWith(RPCConstant.DATAPREFIX)) {
            dataStr = dataStr.substring(dataStr.lastIndexOf("#") + 1);

            String result = new HelloServiceImpl().hello(dataStr);
            ctx.writeAndFlush(result);
        } else {
            System.err.println("[服务端]:协议错误，不解析数据!!!");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

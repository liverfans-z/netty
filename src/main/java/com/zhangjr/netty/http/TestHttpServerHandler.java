package com.zhangjr.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明：
 * 1.SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * 2.HttpObject：客户端和服务器端相互通讯的数据封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取事件触发。读取客户端数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断 msg 是不是 HttpRequest 请求
        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode:" + ctx.hashCode() + ",TestHttpServerHandler:" + this.hashCode());

            System.out.println("msg类型=" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());

            //TODO 这里一开始会收到两次请求，分别为：实际请求和小图标
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求资源为:" + uri.getPath() + ",不做处理");
                return;
            }

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello I am the server.", CharsetUtil.UTF_8);

            //构造一个http响应，即httpResponse
            DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的fullHttpResponse返回
            ctx.writeAndFlush(fullHttpResponse);
        }
    }
}

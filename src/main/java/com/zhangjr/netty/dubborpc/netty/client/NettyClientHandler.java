package com.zhangjr.netty.dubborpc.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2021/01/20 20:30
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;

    /**
     * 调用后返回的结果
     */
    private String result;

    /**
     * 客户端调用方法时传入的参数
     */
    private String param;

    /**
     * 被代理对象调用，发送数据给服务器，发送完毕后等待被唤醒
     * <p>
     * 发送数据给服务器 -> wait -> 等待被唤醒(channelRead) -> 返回结果
     * <p>
     * 调用顺序3
     * 调用顺序5
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("NettyClientHandler,call被调用 1");
        context.writeAndFlush(param);

        //wait,等channelRead方法拿到服务器返回的结果后，唤醒
        wait();
        System.out.println("NettyClientHandler,call被调用 2");
        return result;
    }

    /**
     * 收到服务器数据后，被调用
     * <p>
     * 调用顺序4
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyClientHandler,channelRead被调用");
        result = msg.toString();

        //唤醒等待的线程
        notify();
    }

    /**
     * 连接创建后被调用
     * <p>
     * 调用顺序 1
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyClientHandler,channelActive被调用");
        //因为在其他方法，会使用到 ctx
        context = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 调用顺序2
     *
     * @param param
     */
    void setParameter(String param) {
        System.out.println("NettyClientHandler,setParameter被调用");
        this.param = param;
    }
}

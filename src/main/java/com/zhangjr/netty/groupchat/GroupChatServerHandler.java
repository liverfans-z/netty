package com.zhangjr.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义channel组，管理所有客户端channel                                 //全局事件执行器
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 监听连接建立。一旦连接，第一个被执行
     * <p>
     * 将当前channel加入到channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        /*
        该方法会将ChannelGroup中所有的channel遍历，并发送消息。
        所以我们不需要再次遍历
         */
        //将该客户端加入聊天的信息，推送给其他在线的客户端
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天室\n");

        channelGroup.add(channel);
    }

    /**
     * 断开连接
     * 将xx客户离开信息推送给当前在线的客户
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "退出了聊天室！");

        //触发handlerRemoved时，ChannelGroup会自动把channel从自己中移除，不需要手动remove
        System.out.println("channelGroup.size:" + channelGroup.size());
    }

    /**
     * 表示channel处于活动状态
     * 提示xxx上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    /**
     * 表示channel处于不活动状态
     * 提示xxx离线了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线");
    }

    /**
     * 监听消息，做消息的转发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //这时我们遍历channelGroup，根据同的情况，回送不同的消息。[排除自己]
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("客户[" + channel.remoteAddress() + "]说:" + msg);
            } else {
                //回显一下自己发送的消息
                ch.writeAndFlush("[我]说:" + msg);
            }
        });
    }

    /**
     * 监听异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("客户端:" + ctx.channel().remoteAddress() + ",发生异常:" + cause.toString());
        ctx.close();
    }
}

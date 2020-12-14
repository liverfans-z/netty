package com.zhangjr.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * 自定义Handler，需要继承Netty规定好的某个handler适配器
 * 这时，我们自定义的handler，才能称为一个handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType,显示不同的信息

        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();

            System.out.println("服务端收到数据,id:" + student.getId() + ",姓名:" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();

            System.out.println("服务端收到数据,姓名:" + worker.getName() + ",年龄:" + worker.getAge());
        } else {

            System.err.println("传输类型错误");
        }
    }

    /**
     * 数据处理完毕，给客户端回复
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //write  flush  将数据写入到缓冲，并刷新
        //对发送的数据进行编码，
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 O(∩_∩)O哈哈1111~", StandardCharsets.UTF_8));
    }

    /**
     * 处理异常，一般需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}

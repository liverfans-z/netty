package com.zhangjr.netty.dubborpc.netty.client;

import com.zhangjr.netty.dubborpc.RPCConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2021/01/20 20:44
 */
public class NettyClient {

    /**
     * 创建线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     *
     */
    private static NettyClientHandler client;

    /**
     * 编写方法使用代理模式，获取一个代理对象
     */
    public Object getBean(final Class<?> serviceClass, final String providerName) {

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                ((proxy, method, args) -> {
                    //这段代码，客户端每调用一次service.hello，就会进入到该代码块

                    if (client == null) {
                        initClient();
                    }
                    //设置要发给服务器端的信息
                    client.setParameter(providerName + args[0]);

                    //把client提交到线程池去执行，并得到返回结果。
                    return executorService.submit(client).get();
                }));
    }

    private static void initClient() {
        client = new NettyClientHandler();
        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //不延迟
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });
        try {
            //注意，这里不能写关闭
            bootstrap.connect(RPCConstant.HOSTNAME, RPCConstant.PORT).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

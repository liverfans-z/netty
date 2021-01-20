package com.zhangjr.netty.dubborpc.provider;

import com.zhangjr.netty.dubborpc.RPCConstant;
import com.zhangjr.netty.dubborpc.netty.server.NettyServer;

/**
 * @description: 启动一个服务提供者，就是nettyserver
 * @author: ZhangJR
 * @create: 2021/01/20 20:06
 */
public class ServiceBootstrap {

    public static void main(String[] args) {
        //启动被调用端
        NettyServer.startServer(RPCConstant.HOSTNAME, RPCConstant.PORT);
    }
}

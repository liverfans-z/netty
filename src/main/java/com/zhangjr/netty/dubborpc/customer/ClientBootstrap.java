package com.zhangjr.netty.dubborpc.customer;

import com.zhangjr.netty.dubborpc.RPCConstant;
import com.zhangjr.netty.dubborpc.netty.client.NettyClient;
import com.zhangjr.netty.dubborpc.publicinterface.IHelloService;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2021/01/20 21:01
 */
public class ClientBootstrap {

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();

        //创建代理对象
        IHelloService service = (IHelloService) customer.getBean(IHelloService.class, RPCConstant.DATAPREFIX);

        //通过代理对象，调用服务提供者的方法(服务)
        while (true) {
            String res = service.hello("你好,dubbo!");
            System.out.println("调用结果,服务端返回:[" + res + "]");
            
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

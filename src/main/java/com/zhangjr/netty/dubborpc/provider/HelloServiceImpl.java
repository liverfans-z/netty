package com.zhangjr.netty.dubborpc.provider;

import com.zhangjr.netty.dubborpc.publicinterface.IHelloService;

/**
 * @description:
 * @author: ZhangJR
 * @create: 2021/01/20 20:02
 */
public class HelloServiceImpl implements IHelloService {

    /**
     * 下面打印的count每次都是1
     * 每次调用，服务端创建的都是新对象
     */
    private int count = 0;

    /**
     * 当有消费方调用该方法时，就返回一个结果
     *
     * @param mes
     * @return
     */
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息=" + mes);

        //根据mes返回不同的结果
        if (mes != null) {
            return "你好客户端,我已经收到你的消息:[" + mes + "]," + "第[" + (++count) + "]次";
        } else {
            return "你好客户端,你为什么不说话";
        }
    }
}

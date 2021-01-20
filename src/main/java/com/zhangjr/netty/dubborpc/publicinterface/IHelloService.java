package com.zhangjr.netty.dubborpc.publicinterface;

/**
 * @author ZhangJR
 * @Description: 服务提供方和服务调用方都需要
 */
public interface IHelloService {

    String hello(String mes);
}

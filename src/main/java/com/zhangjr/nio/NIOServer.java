package com.zhangjr.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: 实现客户端与服务器端的非阻塞式简单通讯。服务器端
 * @Author: ZhangJR
 * @CreateDate: 2020/12/1 20:24
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个selector对象
        Selector selector = Selector.open();
        //绑定一个端口6666，在服务器端监听

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector。关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            //这里我们等待1s，如果没有事件发生，就继续
            if(selector.select(1000) == 0){
                //没有事件发生
                System.out.println("服务器等待了1s，无连接");
                continue;
            }
            //如果返回的>0，就获取到相关的selectionKey结合
            //1.表示已经获取到关注的事件
            //2.selector.selectedKeys()返回已关注事件的集合
            //通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();

                //根据key对应的通道发生的事件做相应处理
                if(selectionKey.isAcceptable()){
                    //如果是OP_ACCEPT,表示有新客户端连接
                    //该客户端生成一个socketChannel
                    //这里确定可以拿到想要的东西，所以accept方法不会阻塞
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端已连接！");
                    //将socketChannel也注册到selector，关注事件为OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(selectionKey.isReadable()){
                    //读事件
                    //通过key反向获取channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //设置为非阻塞模式
                    channel.configureBlocking(false);
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端数据为:" + new String(buffer.array()));
                }

                //手动从集合中移除当前的selectionKey。否则会出问题
                keyIterator.remove();
            }
        }
    }
}

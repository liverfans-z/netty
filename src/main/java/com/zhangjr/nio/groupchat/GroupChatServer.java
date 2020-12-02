package com.zhangjr.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author : zhangjr
 * @Decription: 群聊服务端
 *      1.1服务端启动并监听6667端口
 *      1.2服务端接收客户端信息，并实现转发[排除自己][处理上线和离线]
 */
public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    /**
     * 初始化工作
     */
    private GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    public void listen(){
        try {
            while (true){
                int count = selector.select();
                if(count > 0){
                    //有事件要处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //sc注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            //给出提示
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }
                        if(key.isReadable()){
                            //通道是可读状态
                            //处理读......
                            readData(key);
                        }
                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取客户端消息
     *
     * @param key
     */
    private void readData(SelectionKey key){
        //定义一个socketChannel
        SocketChannel channel = null;
        channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int count = channel.read(buffer);
            //根据count的值做处理
            if(count > 0){
                //确实读取到数据
                //把缓冲区数据转换成字符串
                String msg = new String(buffer.array());
                //输出消息
                System.out.println("from 客户端:" + msg);

                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 下线。");

                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //这里会显示远程主机强迫关闭了一个现有的连接
            //e.printStackTrace();
        }
    }

    /**
     * 转发消息给其他通道，(排除自己)
     */
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历 所有注册到selector上的socketChannel，并排除自己
        for (SelectionKey key : selector.keys()) {
            //通过key，取到对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}

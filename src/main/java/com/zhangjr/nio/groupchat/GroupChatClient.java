package com.zhangjr.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author : zhangjr
 * @Decription: 群聊客戶端
 */
public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " is ok!");
    }

    /**
     * 向服务器发消息
     *
     * @param info 消息内容
     */
    public void sendInfo(String info) {
        info = username + "说:" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取从服务器发送过来的消息
     */
    public void readInfo() {
        int readChannels = 0;
        try {
            readChannels = selector.select();
            if (readChannels > 0) {
                //有可用通道。有事件发送是通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        //可读的
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取数据
                        int read = sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                //删除key，防止重复操作
                iterator.remove();
            } else {
                //System.out.println("没有可用的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();
        //启动一个线程，每个3秒，读取从服务器发送数据
        new Thread(() -> {
            while (true){
                groupChatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}

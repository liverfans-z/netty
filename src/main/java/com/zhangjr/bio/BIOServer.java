package com.zhangjr.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: ZhangJR
 * @CreateDate: 2020/11/25 20:53
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        //线程池机制

        //思路
        //1.创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        //2.如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了！");

        while (true){
            //监听，等待客户端连接
            //等待连接时这里会阻塞
            System.out.println("等待连接...........");
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端！");
            //创建线程与之通信
            Runnable runnable = () -> {
                handle(accept);
            };
            executorService.execute(runnable);
        }
    }

    /**
     * 编写方法，与客户端通讯
     * @param socket
     */
    public static void handle(Socket socket){
        byte[] bytes = new byte[1024];
        try {
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            while (true) {
                //连接上之后，不发送数据的话，会一直阻塞在这
                System.out.println("read..................");
                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println("线程id:"+ Thread.currentThread().getId() +",收到客户端数据:" + new String(bytes, 0, read, StandardCharsets.UTF_8));
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接！");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

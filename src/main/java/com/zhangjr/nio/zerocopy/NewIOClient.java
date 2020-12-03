package com.zhangjr.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ZhangJR
 */
public class NewIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));
        String fileName = "D:\\开发软件\\XshellXftp.rar";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();

        //在linux下，一个transferTo函数就可以完成传输
        //windows下，一次调用transferTo最多只能发送8M文件，大文件需要分段传输，而且要注意传输时的位置
        //transferTo底层就是零拷贝
        long l = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送总字节数:" + l + ",耗时:" + (System.currentTimeMillis() - startTime));
    }
}

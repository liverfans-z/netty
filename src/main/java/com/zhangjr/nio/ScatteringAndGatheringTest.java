package com.zhangjr.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Author: ZhangJR
 * @CreateDate: 2020/11/30 21:43
 * @Description:
 *      Scattering：将数据写入到buffer时，可以采用buffer数组，一次写入（分散）
 *      Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        //使用网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        //分配5个字节
        byteBuffers[0] = ByteBuffer.allocate(5);
        //分配3个字节
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        //循环读取
        while (true){
            int byteRead = 0;
            while (byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                //累计读取字节数
                byteRead += l;
                System.out.println("byteread=" + byteRead);

                //使用流打印，看看当前的buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position() + "," +
                        "limit=" + buffer.limit()).forEach(System.out::println);
            }
            //遍历，将所有的buffer反转
            Arrays.asList(byteBuffers).stream().forEach(buffer -> buffer.flip());

            //将数据读出，显示回客户端
            long byteWrite = 0;
            while (byteWrite < messageLength){
                long write = socketChannel.write(byteBuffers);
                byteWrite+=write;
            }

            //将所有buffer clear
            Arrays.asList(byteBuffers).stream().forEach(buffer -> buffer.clear());
            System.out.println("byteRead=" + byteRead + ",byteWrite=" + byteWrite + ",messageLength=" + messageLength);
        }
    }
}

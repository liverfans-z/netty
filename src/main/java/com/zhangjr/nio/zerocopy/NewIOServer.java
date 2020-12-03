package com.zhangjr.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ZhangJR
 */
public class NewIOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (readCount != -1){
                try {
                    readCount = socketChannel.read(byteBuffer);
                }catch (Exception e){
                    //e.printStackTrace();
                    break;
                }
                //倒带，position变成0，mark作废
                byteBuffer.rewind();
            }
        }
    }
}

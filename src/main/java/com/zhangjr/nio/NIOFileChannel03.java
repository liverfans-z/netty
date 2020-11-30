package com.zhangjr.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: 复制文件
 * @Author: ZhangJR
 * @CreateDate: 2020/11/30 20:35
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\ZhangJR\\Desktop\\蛋糕.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel01 = fileInputStream.getChannel();


        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ZhangJR\\Desktop\\蛋糕bak.txt");
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true){
            byteBuffer.clear();

            int read = channel01.read(byteBuffer);
            if(read == -1){
                break;
            }
            byteBuffer.flip();
            channel02.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
        System.out.println("文件复制完毕！");
    }
}

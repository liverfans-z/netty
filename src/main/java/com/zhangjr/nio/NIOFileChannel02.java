package com.zhangjr.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: fileChannel读取文件内容
 * @Author: ZhangJR
 * @CreateDate: 2020/11/30 20:27
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\ZhangJR\\Desktop\\蛋糕.txt");
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        channel.read(byteBuffer);

        System.out.println("读到数据：" + new String(byteBuffer.array()));

        inputStream.close();
    }
}

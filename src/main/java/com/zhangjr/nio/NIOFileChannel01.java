package com.zhangjr.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 将一串字符串写入文件
 * @Author: ZhangJR
 * @CreateDate: 2020/11/26 21:54
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello zhangjr!";
        //创建输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:/file01.txt");

        //通过输出流获取对应的文件channel
        //fileChannel的真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入到byteBuffer中
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));

        //对byteBuffer进行翻转
        byteBuffer.flip();

        //将byteBuffer写入到fileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
        System.out.println("操作完成！");
    }
}

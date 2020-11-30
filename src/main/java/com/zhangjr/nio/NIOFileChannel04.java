package com.zhangjr.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: ZhangJR
 * @CreateDate: 2020/11/30 20:51
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ZhangJR\\Desktop\\A Sunny Day.mp3");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\ZhangJR\\Desktop\\A Sunny Day bak.mp3");

        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();


        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        sourceChannel.close();
        fileInputStream.close();
        destChannel.close();
        fileOutputStream.close();
        System.out.println("文件筐拷贝结束！");
    }
}

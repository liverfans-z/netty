package com.zhangjr.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: MappedByteBuffer：可让文件直接在内存(堆外内存)修改，操作系统不需要拷贝一次
 * @Author: ZhangJR
 * @CreateDate: 2020/11/30 21:19
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\ZhangJR\\Desktop\\1212.txt", "rw");

        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的是读写模式
         * 参数2：0：以直接修改的起始位置
         * 参数3：5：是映射到内存的大小(不是索引位置)，即文件是以多少个字节映射到内存
         * 可以修改范围就是0-5
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        //第一个字节，改成H
        map.put(0, (byte) 'H');
        //第4个字节，改成9
        map.put(3, (byte) '9');

        randomAccessFile.close();

        System.out.println("修改成功！！");
    }
}

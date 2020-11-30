package com.zhangjr.nio;

import java.nio.IntBuffer;

/**
 * @Description:
 * @Author: ZhangJR
 * @CreateDate: 2020/11/25 21:50
 */
public class BasicBuffer {

    public static void main(String[] args) {
        //举例说明buffer的使用
        //创建一个buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer中存放数据
        intBuffer.put(10);
        intBuffer.put(11);
        intBuffer.put(12);
        intBuffer.put(13);
        intBuffer.put(14);

        //从buffer中读数据
        //将buffer转换，读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()){
            System.out.println("取出数据:" + intBuffer.get());
        }
    }
}

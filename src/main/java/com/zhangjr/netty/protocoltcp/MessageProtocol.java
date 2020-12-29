package com.zhangjr.netty.protocoltcp;

/**
 * @description:自定义协议解决TCP粘包拆包的问题
 * @author: ZhangJR
 * @create: 2020/12/29 20:48
 */
public class MessageProtocol {

    private Integer len;
    private byte[] content;

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}

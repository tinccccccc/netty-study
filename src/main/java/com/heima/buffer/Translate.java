package com.heima.buffer;

import com.heima.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 字符串和byteBuffer转换的三种方式
 */
public class Translate {
    public static void main(String[] args) {
        //准备两个字符串
        String str1 = "hello";
        String str2 = "";

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(str1.getBytes());
        ByteBufferUtil.debugAll(buffer);

        //将缓冲区中的数据转换为字符串
        //切换模式
        buffer.flip();

        //通过StandardCharsets 解码，获得CharBuffer,在通过toString获得字符串
        str2 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str2);
        ByteBufferUtil.debugAll(buffer);
    }
}

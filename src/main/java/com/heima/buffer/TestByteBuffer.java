package com.heima.buffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {
    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);

            while (true){
                int len = channel.read(buffer);
                if (len == -1) break;

                //切换至读模式
                buffer.flip();

                while (buffer.hasRemaining()){
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                buffer.clear();//切换为写模式
            }
        } catch (IOException e) {
        }
    }
}

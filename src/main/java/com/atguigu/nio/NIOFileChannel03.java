package com.atguigu.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {

    public static void main(String[] args) throws IOException {

        File file = new File("D:\\hello.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel01 = fileInputStream.getChannel();

        File newFile = new File("D:\\hello2.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            byteBuffer.clear();

            int read = channel01.read(byteBuffer);
            if (read == -1) break;

            byteBuffer.flip();

            channel02.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}

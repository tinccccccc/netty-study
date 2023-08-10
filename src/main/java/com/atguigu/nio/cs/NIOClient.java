package com.atguigu.nio.cs;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws Exception{

        //得到一个通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务端的ip 和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){

            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作...");
            }
        }

        //连接成功,发送数据
        String str = "hello 小猪";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //将 buffer 写入 channel
        socketChannel.write(buffer);

        System.in.read();

    }
}

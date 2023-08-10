package com.atguigu.nio.cs;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception{

        //创建 serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建 selector
        Selector selector = Selector.open();

        //绑定一个端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到 selector ，并关注时间  OP_ACCEPT(连接事件)
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){

            if (selector.select(1000) == 0){
                System.out.println("等待了1秒钟，无连接！！！");
                continue;
            }

            /**
             *   如果返回 > 0 ，就获取到相关的 selectionKey 集合
             *
             *  1.  如果返回 > 0 , 表示已经获取到关注的事件（即前面设置的连接事件OP_ACCEPT）
             *  2.  selector.selectedKeys(); 返回关注事件的集合。
             *  3.  通过 selectionKeys 反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 selectionKeys
            for (SelectionKey selectionKey : selectionKeys) {

                //根据key 对应的通道发生的事件做相应的处理
                if (selectionKey.isAcceptable()){   //如果是 OP_ACCEPT , 有新的客户端连接
                    //该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将 socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功，生成了一个 socketChannel" + socketChannel.hashCode());
                    //将 socketChannel 注册到 selector，关注的事件为 OP_READ，同时给 socketChannel 关联一个 Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()){ //发生了可读事件（此事件就是在发生连接后，我们又注册了 OP_READ 事件）
                    //通过 key 反向获取到对应的 channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取到该 channel 关联的 buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端接收到消息 : " + new String(buffer.array()));
                }

                //手动从集合中移动当前的 selectionKey，防止重复操作
                selectionKeys.remove(selectionKey);
            }


        }





    }
}

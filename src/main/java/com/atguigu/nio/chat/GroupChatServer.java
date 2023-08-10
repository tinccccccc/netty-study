package com.atguigu.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该 listenChannel 注册到 selector 上，并关注连接事件
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){

        try {
            while (true){
                int count = selector.select(2000);
                if (count > 0){//有事件要处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {

                        //监听到有连接
                        if (selectionKey.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将 socketChannel 注册到 selector
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }

                        //监听到可读事件
                        if (selectionKey.isReadable()){
                            readData(selectionKey);

                        }
                        selectionKeys.remove(selectionKey);
                    }
                }else {
//                    System.out.println("等待上线...");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

    //读取客户端信息
    public void readData(SelectionKey key){

        //取到关联的channel
        SocketChannel socketChannel = null;

        try {
            socketChannel = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = socketChannel.read(buffer);

            if (count > 0){
                String msg = new String(buffer.array());
                System.out.println("from 客户端 ：" + msg);

                //向其他客户端发送消息
                sendMsgToOtherClient(msg,socketChannel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + "已离线...");
                //取消注册
                key.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }
    }

    public void sendMsgToOtherClient(String msg, SocketChannel self) throws IOException {

        System.out.println("服务器转发消息中");

        //遍历所有注册到 selector 上的 socketChannel,并排除 self
        for (SelectionKey selectedKey : selector.selectedKeys()) {

            Channel targetChannel = selectedKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self){

                SocketChannel dest = (SocketChannel)targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

}

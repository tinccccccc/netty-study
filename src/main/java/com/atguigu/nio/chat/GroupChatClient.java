package com.atguigu.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    //定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {

        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");
    }


    public void sendMsg(String info){

        info = username + "说" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo(){

        try {
            int readChannels = selector.select();
            if (readChannels > 0){
                Set<SelectionKey> selectedKeys =  selector.selectedKeys();
                for (SelectionKey selectedKey : selectedKeys) {

                    if (selectedKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectedKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    selectedKeys.remove(selectedKey);
                }
            }else {
                System.out.println("没有可用的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException {
        GroupChatClient chatClient = new GroupChatClient();

        new Thread(()->{
            while (true){
                try {
                    chatClient.readInfo();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendMsg(s);
        }
    }


}

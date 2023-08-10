package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true){
            //监听，等待客户端连接(阻塞的)
            Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程与之通信
            executorService.execute(() -> {
                //可以和客户端通讯
                handler(accept);
            });

        }

    }

    /**
     * 和客户端通讯
     */
    public static void handler(Socket socket){

        byte[] bytes = new byte[1024];
        //通过 socket 和获取输入流
        try {
            InputStream inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
                if (read != -1){
                    //输出客户端发送的数据
                    System.out.println(new String(bytes,0,read));

                }else {
                    break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和 client 的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }




}

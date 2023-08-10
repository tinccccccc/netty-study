package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializen extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //1.加入一个Netty 提供的 httpservercodec
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        //2.增加一个自定义的 Handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}

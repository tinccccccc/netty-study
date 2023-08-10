package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 我们自定义一个 handler 需要继续 netty 规定好的某个 HandlerAdapter(规范)
 * 我们自定义的一个 handler 才能称之为handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据
     *
     * @param ctx   上下文对象，含有 管道 pipeline，管道channel，地址
     * @param msg   客户端发送的消息，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {




        ctx.channel().eventLoop().execute(()->{
            ctx.writeAndFlush(Unpooled.copiedBuffer( "hello,客户端222",CharsetUtil.UTF_8));
        });
//        System.out.println("server ctx =" + ctx);
//
//        //将 msg 转成 byteBuf
//        ByteBuf buf = (ByteBuf) msg;
//
//        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端的地址：" + ctx.channel().remoteAddress()  );
    }


    /**
     *   数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         *  writeAndFlush  =  write  + flush
         *  将数据写入缓存，并刷新，对发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端狗~",CharsetUtil.UTF_8));
    }

    /**
     * 异常处理，一般需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

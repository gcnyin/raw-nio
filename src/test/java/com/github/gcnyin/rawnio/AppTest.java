package com.github.gcnyin.rawnio;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class AppTest {
  public static void main(String[] args) throws InterruptedException {
    NioEventLoopGroup worker = new NioEventLoopGroup(1);
    try {
      Bootstrap b = new Bootstrap();
      b.group(worker)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
            ch.pipeline().addLast(new SimpleHandler());
          }
        });
      ChannelFuture f = b.connect("localhost", 8080).sync();
      f.channel().closeFuture().sync();
    } finally {
      worker.shutdownGracefully();
    }
  }

  private static class SimpleHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      ByteBuf buf = ctx.alloc().buffer();
      buf.writeBytes("Hello, world!".getBytes());
      ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }
}

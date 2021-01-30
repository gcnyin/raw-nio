package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ClientBootstrap {
  private InetSocketAddress inetSocketAddress;
  private SocketEventLoop socketEventLoop;
  private SocketHandlerProvider socketHandlerProvider;

  public CompletableFuture<String> connect(InetSocketAddress inetSocketAddress) throws IOException {
    if (socketHandlerProvider == null) {
      throw new RuntimeException("socketHandlerProvider is null");
    }
    this.inetSocketAddress = inetSocketAddress;
    SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
    this.socketEventLoop = new SocketEventLoop(new CountDownLatch(0));
    this.socketEventLoop.setSocketHandlerProvider(socketHandlerProvider);
    this.socketEventLoop.add(socketChannel);
    return this.socketEventLoop.loop();
  }

  public void setSocketHandlerProvider(SocketHandlerProvider socketHandlerProvider) {
    this.socketHandlerProvider = socketHandlerProvider;
  }
}

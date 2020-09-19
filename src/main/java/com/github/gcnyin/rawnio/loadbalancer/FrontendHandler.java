package com.github.gcnyin.rawnio.loadbalancer;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

@Slf4j
public class FrontendHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(512);
  private final SocketContext ctx;
  @Setter
  private BackendHandler backendHandler;
  private final Server server;

  public FrontendHandler(SocketContext socketContext, Server server) {
    this.ctx = socketContext;
    this.server = server;
  }

  @Override
  public void onRegistered() throws IOException {
    SocketChannel backendSocket = SocketChannel.open(new InetSocketAddress(server.getHost(), server.getPort()));
    backendSocket.configureBlocking(false);
    SelectionKey key = backendSocket.register(ctx.getSelector(), SelectionKey.OP_READ);
    BackendHandler backendHandler = new BackendHandler(new SocketContext(backendSocket, key, ctx.getThread(), UUID.randomUUID().toString(), ctx.getSelector()));
    key.attach(backendHandler);
    backendHandler.onRegistered();
    backendHandler.setFrontendHandler(this);
    this.backendHandler = backendHandler;
    ctx.getSelector().wakeup();
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      this.onClose();
      return;
    }
    buffer.flip();
    backendHandler.write(buffer);
  }

  @Override
  public void write(ByteBuffer buffer) throws IOException {
    int i = ctx.getSocketChannel().write(buffer);
    log.info("write {}", i);
    if (buffer.hasRemaining()) {
      buffer.flip();
    } else {
      buffer.clear();
    }
  }

  @Override
  public void onClose() throws IOException {
    ctx.getSocketChannel().close();
    log.info("ID: {}, connection closed", ctx.getConnectionId());
    backendHandler.onClose();
  }
}

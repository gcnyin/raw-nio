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
  private ByteBuffer buffer;
  private final SocketContext ctx;
  @Setter
  private BackendHandler backendHandler;
  private final Server server;
  private boolean firstTime = true;

  public FrontendHandler(SocketContext socketContext, Server server) {
    this.ctx = socketContext;
    this.server = server;
  }

  @Override
  public void onRegistered() throws IOException {
    ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);

    SocketChannel backendSocket = SocketChannel.open(new InetSocketAddress(server.getHost(), server.getPort()));
    backendSocket.configureBlocking(false);
    SelectionKey key = backendSocket.register(ctx.getSelector(), SelectionKey.OP_READ);
    BackendHandler backendHandler = new BackendHandler(new SocketContext(backendSocket, key, ctx.getThread(), UUID.randomUUID().toString(), ctx.getSelector(), ctx.getByteBufferPool()));
    key.attach(backendHandler);
    backendHandler.onRegistered();
    backendHandler.setFrontendHandler(this);
    this.backendHandler = backendHandler;
    ctx.getSelector().wakeup();
  }

  @Override
  public void onRead() throws IOException {
    if (firstTime) {
      firstTime();
    }
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      this.close();
      return;
    }
    buffer.flip();
    backendHandler.write(buffer);
  }

  @Override
  public void write(ByteBuffer buffer) throws IOException {
    if (firstTime) {
      firstTime();
    }
    int i = ctx.getSocketChannel().write(buffer);
    log.info("ID: {}, write {} bytes", ctx.getConnectionId(), i);
    if (buffer.hasRemaining()) {
      buffer.flip();
    } else {
      buffer.clear();
    }
  }

  @Override
  public void close() throws IOException {
    ctx.getByteBufferPool().returnObject(buffer);
    ctx.getSocketChannel().close();
    server.release();
    log.info("release server {}", server);
    log.info("ID: {}, connection closed", ctx.getConnectionId());
    backendHandler.close();
  }

  private void firstTime() {
    this.buffer = ctx.getByteBufferPool().borrowObject();
    firstTime = false;
  }
}

package com.github.gcnyin.rawnio.loadbalancer;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
public class BackendHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(512);
  private final SocketContext ctx;
  @Setter
  private FrontendHandler frontendHandler;

  public BackendHandler(SocketContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("ID: {}, connection closed", ctx.getConnectionId());
      return;
    }
    buffer.flip();
    frontendHandler.write(buffer);
  }

  @Override
  public void write(ByteBuffer buffer) throws IOException {
    ctx.getSocketChannel().write(buffer);
    if (buffer.hasRemaining()) {
      buffer.flip();
    } else {
      buffer.clear();
    }
  }
}

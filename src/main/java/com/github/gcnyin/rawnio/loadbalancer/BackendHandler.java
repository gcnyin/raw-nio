package com.github.gcnyin.rawnio.loadbalancer;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BackendHandler implements SocketHandler {
  private static final Logger log = LoggerFactory.getLogger(BackendHandler.class);

  private ByteBuffer buffer;
  private final SocketContext ctx;
  private FrontendHandler frontendHandler;
  private boolean firstTime = true;

  public BackendHandler(SocketContext ctx) {
    this.ctx = ctx;
  }

  public void setFrontendHandler(FrontendHandler frontendHandler) {
    this.frontendHandler = frontendHandler;
  }

  @Override
  public void onRead() throws IOException {
    if (firstTime) {
      firstTime();
    }
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("ID: " + ctx.getConnectionId() + ", connection closed");
      return;
    }
    buffer.flip();
    frontendHandler.write(buffer);
  }

  @Override
  public void write(ByteBuffer buffer) throws IOException {
    if (firstTime) {
      firstTime();
    }
    int i = ctx.getSocketChannel().write(buffer);
    if (buffer.hasRemaining()) {
      buffer.flip();
    } else {
      buffer.clear();
    }
    log.info("ID: " + ctx.getConnectionId() + ", read " + i + " bytes");
  }

  @Override
  public void close() throws IOException {
    ctx.getByteBufferPool().returnObject(buffer);
    ctx.getSocketChannel().close();
    log.info("ID: " + ctx.getConnectionId() + ", connection closed");
  }

  private void firstTime() {
    buffer = ctx.getByteBufferPool().borrowObject();
    firstTime = false;
  }
}

package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

@Slf4j
public class EchoServerHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(512);
  private final SocketContext ctx;

  public EchoServerHandler(SocketContext socketContext) {
    this.ctx = socketContext;
  }

  @Override
  public void onRegistered() throws IOException {
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("ID: {}, connection closed", ctx.getConnectionId());
      return;
    }
    log.info("ID: {}, read", ctx.getConnectionId());
    ctx.getSelectionKey().interestOps(SelectionKey.OP_WRITE);
  }

  @Override
  public void onWrite() throws IOException {
    buffer.flip();
    ctx.getSocketChannel().write(buffer);
    if (buffer.hasRemaining()) {
      buffer.compact();
    } else {
      buffer.clear();
    }
    log.info("ID: {}, write", ctx.getConnectionId());
    ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);
  }
}

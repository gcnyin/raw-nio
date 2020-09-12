package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

@Slf4j
public class EchoHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(512);
  private final SocketContext ctx;

  public EchoHandler(SocketContext socketContext) {
    this.ctx = socketContext;
  }

  @Override
  public SocketContext getContext() {
    return null;
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("connection closed");
      return;
    }
    log.info("read");
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
    log.info("write");
    ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);
  }
}

package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

@Slf4j
public class EchoClientHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(1024);
  private final SocketContext ctx;

  public EchoClientHandler(SocketContext socketContext) {
    this.ctx = socketContext;
  }

  @Override
  public void onRegistered() throws IOException {
    String s = "Hello, world!";
    buffer.put(s.getBytes());
    buffer.flip();
    ctx.getSocketChannel().write(buffer);
    if (buffer.hasRemaining()) {
      buffer.compact();
    } else {
      buffer.clear();
    }
    log.info("onRegistered");
    ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      log.info("ID: {}, connection closed", ctx.getConnectionId());
    }
    log.info("onRead");
    ctx.close();
  }

  @Override
  public void onWrite() throws IOException {
  }
}

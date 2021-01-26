package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class EchoClientHandler implements SocketHandler {
  private static final Logger log = LoggerFactory.getLogger(EchoClientHandler.class);

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
      log.info("ID: " + ctx.getConnectionId() + ", connection closed");
    }
    log.info("onRead");
    ctx.close();
  }

  @Override
  public void onWrite() throws IOException {
  }
}

package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class EchoServerHandler implements SocketHandler {
  private final static Logger log = LoggerFactory.getLogger(EchoServerHandler.class);

  private final ByteBuffer buffer = ByteBuffer.allocate(512);
  private final SocketContext ctx;

  public EchoServerHandler(SocketContext socketContext) {
    this.ctx = socketContext;
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("ID: " + ctx.getConnectionId() + ", connection closed");
      return;
    }
    log.info("ID: " + ctx.getConnectionId() + ", read " + i + " bytes");
    ctx.getSelectionKey().interestOps(SelectionKey.OP_WRITE);
  }

  @Override
  public void onWrite() throws IOException {
    buffer.flip();
    int i = ctx.getSocketChannel().write(buffer);
    if (buffer.hasRemaining()) {
      buffer.compact();
    } else {
      buffer.clear();
    }
    log.info("ID: " + ctx.getConnectionId() + ", write " + i + " bytes");
    ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);
  }
}

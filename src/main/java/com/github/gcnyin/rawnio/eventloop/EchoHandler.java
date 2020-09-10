package com.github.gcnyin.rawnio.eventloop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Slf4j
public class EchoHandler extends SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(512);

  public EchoHandler(SocketChannel socketChannel, SelectionKey key) {
    super(socketChannel, key);
  }

  @Override
  public void read() throws IOException {
    int i = socketChannel.read(buffer);
    if (i == -1) {
      socketChannel.close();
      log.info("connection closed");
      return;
    }
    log.info("read");
    key.interestOps(SelectionKey.OP_WRITE);
  }

  @Override
  public void write() throws IOException {
    buffer.flip();
    socketChannel.write(buffer);
    if (buffer.hasRemaining()) {
      buffer.compact();
    } else {
      buffer.clear();
    }
    log.info("write");
    key.interestOps(SelectionKey.OP_READ);
  }
}

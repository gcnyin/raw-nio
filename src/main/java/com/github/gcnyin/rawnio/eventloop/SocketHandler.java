package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class SocketHandler {
  protected final SocketChannel socketChannel;
  protected final SelectionKey key;

  public SocketHandler(SocketChannel socketChannel, SelectionKey key) {
    this.socketChannel = socketChannel;
    this.key = key;
  }

  abstract void read() throws IOException;

  abstract void write() throws IOException;
}

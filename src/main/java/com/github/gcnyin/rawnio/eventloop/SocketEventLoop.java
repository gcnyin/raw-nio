package com.github.gcnyin.rawnio.eventloop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class SocketEventLoop {

  private final Selector selector;

  public SocketEventLoop() throws IOException {
    this.selector = Selector.open();
  }

  public synchronized void add(SocketChannel socketChannel, SocketHandlerProvider socketHandlerProvider) throws IOException {
    SelectionKey key = socketChannel
      .configureBlocking(false)
      .register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
    SocketHandler handler = socketHandlerProvider.provide(new SocketContext(socketChannel, key, Thread.currentThread()));
    key.attach(handler);
    selector.wakeup();
  }

  public void loop() {
    log.info("started");
    while (!Thread.interrupted()) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          SocketHandler handler = (SocketHandler) key.attachment();
          if (key.isReadable()) {
            handler.onRead();
          } else if (key.isWritable()) {
            handler.onWrite();
          }
          iter.remove();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

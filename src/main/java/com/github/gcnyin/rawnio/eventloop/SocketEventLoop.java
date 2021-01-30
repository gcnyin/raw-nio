package com.github.gcnyin.rawnio.eventloop;

import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;
import com.github.gcnyin.rawnio.objectpool.ByteBufferPool;
import lombok.Setter;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class SocketEventLoop {
  private static final Logger log = LoggerFactory.getLogger(SocketEventLoop.class);

  private final Selector selector;
  @Setter
  private SocketHandlerProvider socketHandlerProvider;
  private final CountDownLatch countDownLatch;
  private final ByteBufferPool byteBufferPool = new ByteBufferPool();

  public SocketEventLoop(CountDownLatch countDownLatch) throws IOException {
    this.selector = Selector.open();
    this.countDownLatch = countDownLatch;
  }

  public synchronized void add(SocketChannel socketChannel) throws IOException {
    this.add(socketChannel, this.socketHandlerProvider);
  }

  public void add(SocketChannel socketChannel, SocketHandlerProvider socketHandlerProvider) throws IOException {
    SelectionKey key = socketChannel
      .configureBlocking(false)
      .register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    String connectionId = UUID.randomUUID().toString();
    SocketContext socketContext = new SocketContext(socketChannel, key, Thread.currentThread(), connectionId, selector, byteBufferPool);
    SocketHandler handler = socketHandlerProvider.provide(socketContext);
    key.attach(handler);
    handler.onRegistered();
    selector.wakeup();
  }

  public CompletableFuture<String> loop() {
    log.info("started");
    countDownLatch.countDown();
    while (!Thread.interrupted()) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          SocketHandler handler = (SocketHandler) key.attachment();
          try {
            if (key.isReadable()) {
              handler.onRead();
            } else if (key.isWritable()) {
              handler.onWrite();
            }
          } catch (IOException e) {
            log.error("error", e);
            key.channel().close();
          }
          iter.remove();
        }
      } catch (IOException e) {
        log.error("error", e);
        return CompletableFuture.supplyAsync(() -> {
          throw new RuntimeException(e);
        });
      }
    }
    CompletableFuture<String> future = new CompletableFuture<>();
    future.complete("interrupted");
    return future;
  }
}

package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SocketEventLoopGroup {
  private final List<SocketEventLoop> loops;
  private int position = 0;

  public SocketEventLoopGroup(int size) throws IOException, InterruptedException {
    this.loops = new ArrayList<>();
    CountDownLatch countDownLatch = new CountDownLatch(size);
    for (int i = 0; i < size; i++) {
      SocketEventLoop loop = new SocketEventLoop(countDownLatch);
      Thread thread = new Thread(loop::loop);
      thread.setName("event-loop-" + i);
      thread.start();
      this.loops.add(loop);
    }
    boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
    if (!await) {
      throw new RuntimeException("count down latch await timeout");
    }
  }

  public void dispatch(SocketChannel socketChannel, SocketHandlerProvider socketHandlerProvider) throws IOException {
    if (position >= loops.size()) {
      position = 0;
    }
    SocketEventLoop socketEventLoop = loops.get(position);
    socketEventLoop.add(socketChannel, socketHandlerProvider);
    position++;
  }

  public void addEventLoop() throws IOException, InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SocketEventLoop loop = new SocketEventLoop(countDownLatch);
    Thread thread = new Thread(loop::loop);
    thread.setName("event-loop-" + loops.size());
    thread.start();
    boolean await = countDownLatch.await(10, TimeUnit.SECONDS);
    if (!await) {
      throw new RuntimeException("count down latch await timeout");
    }
  }
}

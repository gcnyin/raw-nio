package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class SocketEventLoopGroup {
  private final List<SocketEventLoop> loops;
  private int position = 0;

  public SocketEventLoopGroup(int size) throws IOException {
    this.loops = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      SocketEventLoop loop = new SocketEventLoop();
      Thread thread = new Thread(loop::loop);
      thread.setName("event-loop-" + i);
      thread.start();
      this.loops.add(loop);
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
}

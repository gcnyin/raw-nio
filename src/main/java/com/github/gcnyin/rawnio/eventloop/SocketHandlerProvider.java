package com.github.gcnyin.rawnio.eventloop;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public interface SocketHandlerProvider {
  SocketHandler provide(SocketContext socketContext);
}

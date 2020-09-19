package com.github.gcnyin.rawnio.eventloop;

public interface SocketHandlerProvider {
  SocketHandler provide(SocketContext socketContext);
}

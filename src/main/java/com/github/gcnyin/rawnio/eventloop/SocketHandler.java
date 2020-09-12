package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;

public interface SocketHandler {
  SocketContext getContext();

  void onRead() throws IOException;

  void onWrite() throws IOException;
}

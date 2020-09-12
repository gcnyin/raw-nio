package com.github.gcnyin.rawnio.eventloop;

import java.io.IOException;

public interface SocketHandler {
  void onRegistered() throws IOException;

  void onRead() throws IOException;

  void onWrite() throws IOException;
}

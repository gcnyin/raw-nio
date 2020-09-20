package com.github.gcnyin.rawnio.loadbalancer;

import lombok.Value;

import java.util.concurrent.atomic.AtomicInteger;

@Value
public class Server {
  private final String host;
  private final int port;
  private final AtomicInteger connectionCount = new AtomicInteger(0);

  public int getConnectionCount() {
    return connectionCount.intValue();
  }

  public int addReference() {
    return connectionCount.incrementAndGet();
  }

  public int release() {
    return connectionCount.decrementAndGet();
  }
}

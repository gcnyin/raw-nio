package com.github.gcnyin.rawnio.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
  private final String host;
  private final int port;
  private final AtomicInteger connectionCount = new AtomicInteger(0);

  public Server(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public int getConnectionCount() {
    return connectionCount.intValue();
  }

  public void addReference() {
    connectionCount.incrementAndGet();
  }

  public void release() {
    connectionCount.decrementAndGet();
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}

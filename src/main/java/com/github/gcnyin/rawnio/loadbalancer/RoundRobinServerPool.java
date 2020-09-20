package com.github.gcnyin.rawnio.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoundRobinServerPool implements ServerPool {
  private final List<Server> servers = new ArrayList<>();
  private int i = 0;

  public RoundRobinServerPool(Collection<Server> servers) {
    if (servers.size() == 0) {
      throw new RuntimeException("servers size should be greater than 0");
    }
    this.servers.addAll(servers);
  }

  @Override
  public Server getServer() {
    if (i >= servers.size()) {
      i = 0;
    }
    Server server = servers.get(i);
    i++;
    return server;
  }
}

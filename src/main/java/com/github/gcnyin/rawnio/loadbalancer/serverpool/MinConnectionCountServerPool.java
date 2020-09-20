package com.github.gcnyin.rawnio.loadbalancer.serverpool;

import com.github.gcnyin.rawnio.loadbalancer.Server;

import java.util.*;

public class MinConnectionCountServerPool implements ServerPool {
  private final List<Server> servers = new ArrayList<>();

  public MinConnectionCountServerPool(Collection<Server> servers) {
    if (servers.size() == 0) {
      throw new RuntimeException("servers length should be greater than 0");
    }
    this.servers.addAll(servers);
  }

  @Override
  public Server getServer() {
    Optional<Server> optionalServer = servers.stream()
      .sorted(Comparator.comparingInt(Server::getConnectionCount))
      .limit(1)
      .findAny();
    return optionalServer.orElse(servers.get(0));
  }
}

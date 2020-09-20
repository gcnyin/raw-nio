package com.github.gcnyin.rawnio.loadbalancer.serverpool;

import com.github.gcnyin.rawnio.loadbalancer.Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomServerPool implements ServerPool {
  private final Random random = new Random();
  private final List<Server> servers = new ArrayList<>();

  public RandomServerPool(Collection<Server> servers) {
    if (servers.size() == 0) {
      throw new RuntimeException("servers size should be greater than 0");
    }
    this.servers.addAll(servers);
  }

  @Override
  public Server getServer() {
    int i = random.nextInt(servers.size());
    return servers.get(i);
  }
}

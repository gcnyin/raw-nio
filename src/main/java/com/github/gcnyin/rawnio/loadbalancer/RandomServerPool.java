package com.github.gcnyin.rawnio.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomServerPool implements ServerPool {
  private final Random random = new Random();
  private final List<Server> servers = new ArrayList<>();

  public RandomServerPool(Collection<Server> servers) {
    this.servers.addAll(servers);
  }

  @Override
  public Server getServer() {
    int i = random.nextInt(servers.size());
    return servers.get(i);
  }
}

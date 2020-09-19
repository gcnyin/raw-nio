package com.github.gcnyin.rawnio.loadbalancer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ServerPool {
  private final List<Server> serverList = new ArrayList<>();
  private final Random random = new Random();

  public ServerPool add(Server server) {
    serverList.add(server);
    return this;
  }

  public ServerPool addAll(Collection<Server> serverCollection) {
    serverList.addAll(serverCollection);
    return this;
  }

  public Server getServer() {
    int i = random.nextInt(serverList.size());
    return serverList.get(i);
  }
}

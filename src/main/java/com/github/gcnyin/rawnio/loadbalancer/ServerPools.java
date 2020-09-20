package com.github.gcnyin.rawnio.loadbalancer;

import java.util.Collection;

public class ServerPools {
  public static ServerPool randomPool(Collection<Server> servers) {
    return new RandomServerPool(servers);
  }

  public static ServerPool roundRobinPool(Collection<Server> servers) {
    return new RoundRobinServerPool(servers);
  }
}

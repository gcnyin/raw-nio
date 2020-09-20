package com.github.gcnyin.rawnio.loadbalancer.serverpool;

import com.github.gcnyin.rawnio.loadbalancer.Server;

import java.util.Collection;

public class ServerPools {
  public static ServerPool randomPool(Collection<Server> servers) {
    return new RandomServerPool(servers);
  }

  public static ServerPool roundRobinPool(Collection<Server> servers) {
    return new RoundRobinServerPool(servers);
  }

  public static ServerPool minConnectionCountPool(Collection<Server> servers) {
    return new MinConnectionCountServerPool(servers);
  }
}

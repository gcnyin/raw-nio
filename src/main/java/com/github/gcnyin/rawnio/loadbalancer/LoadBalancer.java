package com.github.gcnyin.rawnio.loadbalancer;

import com.github.gcnyin.rawnio.eventloop.ServerBootstrap;
import com.github.gcnyin.rawnio.loadbalancer.serverpool.ServerPool;
import com.github.gcnyin.rawnio.loadbalancer.serverpool.ServerPools;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoadBalancer {
  private static final Logger log = LoggerFactory.getLogger(LoadBalancer.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    log.info(Arrays.toString(args));
    if (args.length < 2) {
      log.error("please input 2 args, such as `8080 localhost:8081,localhost:8082`");
      return;
    }
    int lbPort = Integer.parseInt(args[0]);
    String s = args[1];
    String[] split = s.split(",");
    String lbStrategy = args[2];
    List<Server> servers = Arrays.stream(split)
      .map(LoadBalancer::parseServer).collect(Collectors.toList());
    ServerPool serverPool;
    switch (lbStrategy) {
      case "minConnectionCountPool":
        serverPool = ServerPools.minConnectionCountPool(servers);
        break;
      case "randomPool":
        serverPool = ServerPools.randomPool(servers);
        break;
      case "roundRobinPool":
        serverPool = ServerPools.roundRobinPool(servers);
        break;
      default:
        throw new RuntimeException("lb strategy is wrong");
    }
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
      .provider(socketContext -> {
        Server server = serverPool.getServer();
        server.addReference();
        log.info("get server: " + server);
        return new FrontendHandler(socketContext, server);
      })
      .connect(lbPort);
  }

  private static Server parseServer(String it) {
    String[] sp = it.split(":");
    String host = sp[0];
    int port = Integer.parseInt(sp[1]);
    return new Server(host, port);
  }
}

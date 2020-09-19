package com.github.gcnyin.rawnio.loadbalancer;

import lombok.Value;

@Value
public class Server {
  private final String host;
  private final int port;
}

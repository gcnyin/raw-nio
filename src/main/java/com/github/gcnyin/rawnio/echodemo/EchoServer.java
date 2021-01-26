package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.ServerBootstrap;

import java.io.IOException;

public class EchoServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    if (args.length < 1) {
//      log.error("please input 1 arg, which is the server port");
      return;
    }
    int port = Integer.parseInt(args[0]);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
      .provider(EchoServerHandler::new)
      .connect(port);
  }
}

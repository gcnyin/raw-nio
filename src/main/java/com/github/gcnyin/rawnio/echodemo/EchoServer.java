package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.ServerBootstrap;

import java.io.IOException;

public class EchoServer {
  public static void main(String[] args) throws IOException, InterruptedException {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
      .provider(EchoServerHandler::new)
      .connect(8080);
  }
}

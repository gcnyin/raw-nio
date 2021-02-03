package com.github.gcnyin.rawnio.http1;

import com.github.gcnyin.rawnio.eventloop.ServerBootstrap;
import com.github.gcnyin.rawnio.http1.handler.UriHandler;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;

public class HttpServer {
  private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    if (args.length < 1) {
      log.error("please input 1 arg, which is the server port");
      return;
    }
    int port = Integer.parseInt(args[0]);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap
      .provider(ctx -> new HttpServerHandler(ctx, new UriHandler()))
      .connect(port);
  }
}

package com.github.gcnyin.rawnio.eventloop;

import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerBootstrap {
  private static final Logger log = LoggerFactory.getLogger(ServerBootstrap.class);

  private int port;
  private Selector selector;
  private ServerSocketChannel server;
  private SocketEventLoopGroup eventLoopGroup;
  private SocketHandlerProvider socketHandlerProvider;

  public ServerBootstrap provider(SocketHandlerProvider socketHandlerProvider) {
    this.socketHandlerProvider = socketHandlerProvider;
    return this;
  }

  public void connect(int port) throws IOException, InterruptedException {
    if (socketHandlerProvider == null) {
      throw new RuntimeException("socketHandlerProvider is null");
    }
    selector = Selector.open();
    server = ServerSocketChannel.open();
    eventLoopGroup = new SocketEventLoopGroup(4);
    this.port = port;

    server
      .bind(new InetSocketAddress(this.port))
      .configureBlocking(false)
      .register(selector, SelectionKey.OP_ACCEPT);
    log.info("server started on " + this.port + " port");
    while (!Thread.interrupted()) {
      selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iter = keys.iterator();
      while (iter.hasNext()) {
        SelectionKey key = iter.next();
        if (key.isAcceptable()) {
          SocketChannel channel = server.accept();
          log.info("new connection");
          eventLoopGroup.dispatch(channel, socketHandlerProvider);
        }
        iter.remove();
      }
    }
  }
}

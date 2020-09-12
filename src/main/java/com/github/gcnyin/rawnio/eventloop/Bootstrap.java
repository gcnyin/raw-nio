package com.github.gcnyin.rawnio.eventloop;

import com.github.gcnyin.rawnio.echodemo.EchoHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class Bootstrap {
  private final int port;
  private final Selector selector;
  private final ServerSocketChannel server;
  private final SocketEventLoopGroup eventLoopGroup;

  public Bootstrap(int port) throws IOException {
    this.port = port;
    selector = Selector.open();
    server = ServerSocketChannel.open();
    eventLoopGroup = new SocketEventLoopGroup(4);
  }

  public void start() throws IOException {
    server
      .bind(new InetSocketAddress(port))
      .configureBlocking(false)
      .register(selector, SelectionKey.OP_ACCEPT);
    log.info("server started on {} port", port);
    while (!Thread.interrupted()) {
      selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iter = keys.iterator();
      while (iter.hasNext()) {
        SelectionKey key = iter.next();
        if (key.isAcceptable()) {
          SocketChannel channel = server.accept();
          log.info("new connection");
          eventLoopGroup.dispatch(channel, EchoHandler::new);
        }
        iter.remove();
      }
    }
  }
}

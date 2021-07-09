package com.github.gcnyin.rawnio.rst;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class RstServer {
  public static InetSocketAddress ADDR = new InetSocketAddress(6543);

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel server = ServerSocketChannel.open();

    server.bind(ADDR);
    server.configureBlocking(false);
    server.register(selector, SelectionKey.OP_ACCEPT);

    while (true) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          iter.remove();
          if (key.isAcceptable()) {
            SocketChannel socketChannel = server.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
          } else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.setOption(StandardSocketOptions.SO_LINGER, 0); // send RST. If no such line, socket will send FIN
            channel.close();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
  }
}

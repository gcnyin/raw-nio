package com.github.gcnyin.rawnio.tlv;

import com.github.gcnyin.rawnio.objectpool.ByteBufferPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TlvServer {
  static InetSocketAddress ADDR = new InetSocketAddress(6543);

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();

    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.bind(ADDR);
    serverChannel.configureBlocking(false);
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);

    ByteBufferPool byteBufferPool = new ByteBufferPool();

    while (true) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          if (key.isAcceptable()) {
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(new TlvServerHandler(socketChannel, selector, byteBufferPool));
            selector.wakeup();
          } else if (key.isReadable()) {
            TlvServerHandler handler = (TlvServerHandler) key.attachment();
            handler.read();
          }
          iter.remove();
        }
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
  }
}

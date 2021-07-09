package com.github.gcnyin.rawnio.rst;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class RstClient {
  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    SocketChannel client = SocketChannel.open();
    client.connect(RstServer.ADDR);
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_WRITE);

    while (selector.isOpen()) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            int i = channel.read(ByteBuffer.allocate(1024));
            if (i < 0) {
              System.out.println("fin stop");
              key.cancel();
            }
          } else if (key.isWritable()) {
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
            SocketChannel channel = (SocketChannel) key.channel();
            buf.flip();
            channel.write(buf);
            System.out.println("send");
            key.interestOps(SelectionKey.OP_READ);
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

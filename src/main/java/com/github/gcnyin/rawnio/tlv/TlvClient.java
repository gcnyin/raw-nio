package com.github.gcnyin.rawnio.tlv;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TlvClient {
  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    SocketChannel client = SocketChannel.open();
    client.connect(TlvServer.ADDR);
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_WRITE);

    while (selector.isOpen()) {
      try {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            byte[] bytes = {0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x00, 0x02, 0x68, 0x69}; // Hi
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            channel.write(buffer);
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

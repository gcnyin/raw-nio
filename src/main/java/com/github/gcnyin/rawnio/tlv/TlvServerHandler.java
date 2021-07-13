package com.github.gcnyin.rawnio.tlv;

import com.github.gcnyin.rawnio.objectpool.ByteBufferPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TlvServerHandler {
  public static final int TYPE_STATE = 0;
  public static final int LENGTH_STATE = 1;
  public static final int VALUE_STATE = 2;

  private static final byte[] TYPE_BYTES = new byte[]{0x01, 0x23, 0x45, 0x67};

  private final SocketChannel channel;
  private final ByteBufferPool byteBufferPool;
  private final ByteBuffer buffer;
  /**
   * 0 -> 1
   * 1 -> 2
   * 2 -> 0
   */
  private final Handler[] handlers = new Handler[]{
    this::readType,
    this::readLength,
    this::readValue
  };
  private final byte[] type = new byte[4];
  private byte typePointer = 0;
  private final byte[] length = new byte[4];
  private byte lengthPointer = 0;
  private int dataLength = 0;
  private byte[] data;
  private int dataPointer = 0;
  private int state = TYPE_STATE;

  public TlvServerHandler(SocketChannel channel, ByteBufferPool byteBufferPool) {
    this.channel = channel;
    this.byteBufferPool = byteBufferPool;
    this.buffer = byteBufferPool.borrowObject();
  }

  public void read() throws IOException {
    while (true) {
      int i = channel.read(buffer);
      if (i == -1) {
        close();
        return;
      }
      if (i == 0) return;

      buffer.flip();
      while (buffer.hasRemaining()) {
        byte b = buffer.get();
        int f = handlers[state].feed(b);
        if (f == -1) {
          close();
          return;
        }
      }
      buffer.clear();
    }
  }

  public void close() throws IOException {
    channel.close();
    byteBufferPool.returnObject(buffer);
  }

  interface Handler {
    int feed(byte b);
  }

  private int readType(byte b) {
    type[typePointer] = b;
    if (typePointer != 3) {
      typePointer++;
      return 0;
    }
    typePointer = 0;
    if (!Arrays.equals(type, TYPE_BYTES)) {
      return -1;
    }
    state = LENGTH_STATE;
    return 0;
  }

  private int readLength(byte b) {
    length[lengthPointer] = b;
    if (lengthPointer != 3) {
      lengthPointer++;
      return 0;
    }
    lengthPointer = 0;
    dataLength = ByteBuffer.wrap(length).getInt();
    if (dataLength == 0) {
      return -1;
    }
    data = new byte[dataLength];
    state = VALUE_STATE;
    return 0;
  }

  private int readValue(byte b) {
    data[dataPointer] = b;
    if (dataPointer != dataLength - 1) {
      dataPointer++;
      return 0;
    }
    dataPointer = 0;
    process();
    state = TYPE_STATE;
    return 0;
  }

  private void process() {
    if (data == null) return;
    String s = new String(data, StandardCharsets.UTF_8);
    System.out.println(s);
  }
}

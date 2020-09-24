package com.github.gcnyin.rawnio.collection;

import lombok.Getter;

import java.util.Arrays;

public class ByteArray {
  private byte[] source = {};
  @Getter
  private long size = 0;

  public ByteArray add(byte... bytes) {
    int sourceOriginLength = source.length;
    int bytesLength = bytes.length;
    source = Arrays.copyOf(source, sourceOriginLength + bytesLength);
    System.arraycopy(bytes, 0, source, sourceOriginLength, bytesLength);
    size += bytesLength;
    return this;
  }

  public int getInt(int index) {
    byte first = source[index];
    byte second = source[index + 1];
    byte third = source[index + 2];
    byte forth = source[index + 3];
    return first << 24 | (second & 0xFF) << 16 | (third & 0xFF) << 8 | (forth & 0xFF);
  }

  public byte[] getCopyArray() {
    return source.clone();
  }

  public ByteArray clear() {
    source = new byte[]{};
    size = 0;
    return this;
  }
}

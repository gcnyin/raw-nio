package com.github.gcnyin.rawnio.utils;

import java.util.Arrays;

public class ByteArray {
  private byte[] source = {};

  public ByteArray add(byte... bytes) {
    int sourceOriginLength = source.length;
    int bytesLength = bytes.length;
    source = Arrays.copyOf(source, sourceOriginLength + bytesLength);
    System.arraycopy(bytes, 0, source, sourceOriginLength, bytesLength);
    return this;
  }

  public byte[] getCopy() {
    return source.clone();
  }

  public ByteArray clear() {
    source = new byte[]{};
    return this;
  }
}

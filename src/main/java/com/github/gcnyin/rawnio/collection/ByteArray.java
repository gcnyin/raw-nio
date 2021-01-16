package com.github.gcnyin.rawnio.collection;

import lombok.Getter;

import java.util.Arrays;

public class ByteArray {
  private byte[] source = new byte[10];
  private int size = 0;
  private final double factor = 0.75;
  private int capacity = 10;

  public ByteArray add(byte[] bytes) {
    int newBytesSize = bytes.length;
    if (shouldGrow(newBytesSize)) {
      grow(newBytesSize);
    }
    System.arraycopy(bytes, 0, source, size, newBytesSize);
    size += newBytesSize;
    return this;
  }

  public int getInt(int index) {
    if (index + 4 > size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    byte first = source[index];
    byte second = source[index + 1];
    byte third = source[index + 2];
    byte forth = source[index + 3];
    return first << 24 | (second & 0xFF) << 16 | (third & 0xFF) << 8 | (forth & 0xFF);
  }

  public char getChar(int index) {
    if (index >= size) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return (char) source[index];
  }

  public byte[] getCopyArray() {
    byte[] clone = new byte[size];
    System.arraycopy(source, 0, clone, 0, size);
    return clone;
  }

  public ByteArray clear() {
    source = new byte[10];
    size = 0;
    return this;
  }

  private boolean shouldGrow(long newBytesSize) {
    return size >= capacity * factor || size + newBytesSize >= capacity * factor;
  }

  private void grow(int newBytesSize) {
    int newTotalSize = size + newBytesSize;
    if (newTotalSize >= capacity) {
      capacity = (int) (newTotalSize * (1 + factor));
    } else {
      capacity *= (1 + factor);
    }
    source = Arrays.copyOf(source, capacity);
  }

  public int size() {
    return size;
  }

  public void removeFirst(int count) {
    byte[] newBytes = new byte[size - count];
    System.arraycopy(source, count, newBytes, 0, size - count);
    source = newBytes;
    size -= count;
  }
}

package com.github.gcnyin.rawnio.objectpool;

import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * It's not thread-safe, do not use the same {@link ByteBuffer} instance in different {@link Thread}
 */
public class ByteBufferPool {
  private final Map<ByteBuffer, Boolean> map = new IdentityHashMap<>();
  private int totalCount = 0;
  private int usedCount = 0;

  public int getUsedCount() {
    return usedCount;
  }

  public int getSize() {
    return map.size();
  }

  public ByteBuffer borrowObject() {
    Optional<ByteBuffer> optional = map
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue().equals(false))
      .map(Map.Entry::getKey)
      .findAny();
    usedCount++;
    if (optional.isPresent()) {
      ByteBuffer byteBuffer = optional.get();
      map.put(byteBuffer, true);
      return byteBuffer;
    }
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
    map.put(byteBuffer, true);
    totalCount++;
    return byteBuffer;
  }

  /**
   * @param byteBuffer the borrowed bytebuffer
   * @return true represents success, false represents not such byteBuffer in pool
   */
  public boolean returnObject(ByteBuffer byteBuffer) {
    boolean containsKey = map.containsKey(byteBuffer);
    if (!containsKey) {
      return false;
    }
    byteBuffer.clear();
    map.put(byteBuffer, false);
    usedCount--;
    if (showScaleDown()) {
      scaleDown();
    }
    return true;
  }

  private boolean showScaleDown() {
    return usedCount * 1.0 / totalCount <= 0.5;
  }

  private void scaleDown() {
    List<ByteBuffer> usedByteBuffers = map
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue().equals(false))
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());
    List<ByteBuffer> subList = usedByteBuffers.subList(0, usedByteBuffers.size() / 2);
    subList.forEach(map::remove);
  }

  @Override
  public String toString() {
    return "ByteBufferPool{" +
      "totalCount=" + totalCount +
      ", usedCount=" + usedCount +
      '}';
  }
}

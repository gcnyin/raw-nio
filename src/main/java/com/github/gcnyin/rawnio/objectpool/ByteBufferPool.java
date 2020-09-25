package com.github.gcnyin.rawnio.objectpool;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * It's not thread-safe, do not use the same {@link ByteBuffer} instance in different {@link Thread}
 */
@Slf4j
@ToString
public class ByteBufferPool {
  @ToString.Exclude
  private final Map<ByteBuffer, Boolean> map = new IdentityHashMap<>();
  private int totalCount = 0;
  @Getter
  private int usedCount = 0;

  public int getSize() {
    return map.size();
  }

  public ByteBuffer borrowObject() {
    log.info("pool size before borrow: {}", map.size());
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
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256);
    map.put(byteBuffer, true);
    totalCount++;
    return byteBuffer;
  }

  /**
   * @param byteBuffer the borrowed bytebuffer
   * @return true represents success, false represents not such byteBuffer in pool
   */
  public boolean returnObject(ByteBuffer byteBuffer) {
    log.info("pool size before return: {}", map.size());
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
}

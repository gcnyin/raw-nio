package com.github.gcnyin.rawnio.objectpool;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link ByteBufferPool} uses {@link ThreadLocal}, so do not use the same {@link ByteBufferPool} instance in different {@link Thread}
 */
@Slf4j
public class ByteBufferPool {
  private final Map<ByteBuffer, Boolean> map = new IdentityHashMap<>();

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
    if (optional.isPresent()) {
      ByteBuffer byteBuffer = optional.get();
      map.put(byteBuffer, true);
      return byteBuffer;
    }
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256);
    map.put(byteBuffer, true);
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
    return true;
  }
}

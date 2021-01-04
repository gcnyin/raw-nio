package com.github.gcnyin.rawnio.objectpool;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ByteBufferPoolTest {
  @Test
  public void should_return_same_object_when_borrow_return_and_borrow_again_given_a_ByteBufferPool() {
    ByteBufferPool pool = new ByteBufferPool();
    ByteBuffer b1 = pool.borrowObject();
    boolean b = pool.returnObject(b1);
    assertTrue(b);
    ByteBuffer b2 = pool.borrowObject();

    assertEquals(b1, b2);
    assertEquals(1, pool.getSize());
    assertEquals(1, pool.getUsedCount());
  }

  @Test
  public void should_size_equals_3_when_borrow_3_times_given_a_ByteBufferPool() {
    ByteBufferPool pool = new ByteBufferPool();
    pool.borrowObject();
    pool.borrowObject();
    pool.borrowObject();

    assertEquals(3, pool.getSize());
    assertEquals(3, pool.getUsedCount());
  }

  @Test
  public void should_scale_down_when_borrow_3_buffer_and_reutrn_all_of_them_given_a_ByteBufferPool() {
    ByteBufferPool pool = new ByteBufferPool();
    ByteBuffer b1 = pool.borrowObject();
    ByteBuffer b2 = pool.borrowObject();
    ByteBuffer b3 = pool.borrowObject();
    pool.returnObject(b1);
    pool.returnObject(b2);
    pool.returnObject(b3);

    assertEquals(1, pool.getSize());
    assertEquals(0, pool.getUsedCount());
  }
}

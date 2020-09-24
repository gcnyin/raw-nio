package com.github.gcnyin.rawnio.objectpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

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
  }

  @Test
  public void should_size_equals_3_when_borrow_3_times_given_a_ByteBufferPool() {
    ByteBufferPool pool = new ByteBufferPool();
    pool.borrowObject();
    pool.borrowObject();
    pool.borrowObject();

    assertEquals(3, pool.getSize());
  }

  @Test
  public void test_apache_common_pool() throws Exception {
    GenericObjectPool<StringBuffer> pool = new GenericObjectPool<>(new StringBufferFactory());
    StringBuffer stringBuffer = pool.borrowObject();
    pool.returnObject(stringBuffer);
    StringBuffer stringBuffer1 = pool.borrowObject();
    assertEquals(stringBuffer, stringBuffer1);

    StringBuffer s1 = pool.borrowObject();
    StringBuffer s2 = pool.borrowObject();
    assertNotEquals(s1, s2);

  }

  static class StringBufferFactory extends BasePooledObjectFactory<StringBuffer> {
    @Override
    public StringBuffer create() {
      return new StringBuffer();
    }

    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer buffer) {
      return new DefaultPooledObject<>(buffer);
    }

    @Override
    public void passivateObject(PooledObject<StringBuffer> pooledObject) {
      pooledObject.getObject().setLength(0);
    }
  }
}

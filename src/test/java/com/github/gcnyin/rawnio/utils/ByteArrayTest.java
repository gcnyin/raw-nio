package com.github.gcnyin.rawnio.utils;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ByteArrayTest {
  @Test
  public void should_return_inserted_data_when_insert_some_bytes_and_call_getCopy_given_a_ByteArrry() {
    ByteArray byteArray = new ByteArray();
    byteArray.add(new byte[]{1, 2, 3})
      .add(new byte[]{4, 5, 6});
    byte[] copy = byteArray.getCopy();
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, copy);
  }
}

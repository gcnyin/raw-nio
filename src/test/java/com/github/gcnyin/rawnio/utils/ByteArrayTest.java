package com.github.gcnyin.rawnio.utils;

import com.github.gcnyin.rawnio.collection.ByteArray;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteArrayTest {
  @Test
  public void should_return_inserted_data_when_insert_some_bytes_and_call_getCopyArray_given_a_ByteArray() {
    ByteArray byteArray = new ByteArray();
    byteArray.add(new byte[]{1, 2, 3})
      .add(new byte[]{4, 5, 6});
    byte[] copy = byteArray.getCopyArray();
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, copy);
    assertEquals(6, byteArray.size());
  }

  @Test
  public void should_clear_data_when_call_clear_given_a_ByteArray() {
    ByteArray byteArray = new ByteArray();
    byteArray.add(new byte[]{1, 2, 3})
      .add(new byte[]{4, 5, 6});
    byte[] copy = byteArray.clear().getCopyArray();
    assertArrayEquals(new byte[]{}, copy);
    assertEquals(0, byteArray.size());
  }

  @Test
  public void should_return_int_when_call_getInt_given_a_ByteArray_contains_4_bytes() {
    ByteArray byteArray = new ByteArray();
    int i1 = byteArray.add(new byte[]{1, 2, 3, 4}).getInt(0);
    assertEquals(16909060, i1);
    int i2 = byteArray.add(new byte[]{5}).getInt(1);
    assertEquals(33752069, i2);
  }

  @Test
  public void should_throw_ArrayIndexOutOfBoundsException_when_call_getInt_with_param_1_given_a_ByteArray_only_contains_4_bytes() {
    ByteArray byteArray = new ByteArray();
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
      byteArray.add(new byte[]{1, 2, 3, 4}).getInt(1);
    });
  }

  @Test
  public void should_remove_bytes() {
    ByteArray byteArray = new ByteArray();
    byteArray.add(new byte[]{1, 2, 3});
    byteArray.removeFirst(2);
    assertEquals(1, byteArray.size());
  }

  @Test
  public void should_get_char() {
    ByteArray byteArray = new ByteArray();
    byteArray.add(new byte[]{97, 48});
    assertEquals('a', byteArray.getChar(0));
    assertEquals('0', byteArray.getChar(1));
  }
}

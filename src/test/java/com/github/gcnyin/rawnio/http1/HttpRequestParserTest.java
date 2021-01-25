package com.github.gcnyin.rawnio.http1;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertTrue;

public class HttpRequestParserTest {
  @Test
  public void should_parse_normal_request_success() {
    HttpRequestParser parser = new HttpRequestParser();
    String request = "GET / HTTP/1.1\r\n"
      + "host: www.example.com\r\n"
      + "cache-control: max-age=0\r\n" +
      "\r\n";
    ByteBuffer byteBuffer = ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8));
    parser.read(byteBuffer);
    assertTrue(parser.isReady());
  }
}

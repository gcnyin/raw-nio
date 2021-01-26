package com.github.gcnyin.rawnio.http1;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class HttpRequestParserTest {
  @Test
  public void should_parse_get_request_success() {
    HttpRequestParser parser = new HttpRequestParser();
    String r = "GET / HTTP/1.1\r\n"
      + "host: www.example.com\r\n"
      + "cache-control: max-age=0\r\n" +
      "\r\n";
    ByteBuffer byteBuffer = ByteBuffer.wrap(r.getBytes(StandardCharsets.UTF_8));

    parser.read(byteBuffer);
    assertTrue(parser.isReady());

    HttpRequest request = parser.getHttpRequest();
    assertEquals("/", request.getUri());
    assertNull(request.getBody());
    assertEquals("HTTP/1.1", request.getVersion());
    HttpHeaders httpHeaders = request.getHttpHeaders();
    assertEquals(2, httpHeaders.size());
    List<HttpHeader> headerList = httpHeaders.toSortedList();

    HttpHeader h1 = headerList.get(0);
    assertEquals("cache-control", h1.getKey());
    assertEquals("max-age=0", h1.getValue());

    HttpHeader h2 = headerList.get(1);
    assertEquals("host", h2.getKey());
    assertEquals("www.example.com", h2.getValue());
  }
}

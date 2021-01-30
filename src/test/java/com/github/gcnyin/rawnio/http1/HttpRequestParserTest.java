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
      + "cache-control: max-age=0\r\n"
      + "\r\n";
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

  @Test
  public void should_parse_post_request_success() {
    HttpRequestParser parser = new HttpRequestParser();
    String r = "POST /user HTTP/1.1\r\n"
      + "host: www.example.com\r\n"
      + "content-type: application/json\r\n"
      + "content-length: 16\r\n"
      + "\r\n"
      + "{\"name\":\"admin\"}";
    ByteBuffer byteBuffer = ByteBuffer.wrap(r.getBytes(StandardCharsets.UTF_8));

    parser.read(byteBuffer);
    assertTrue(parser.isReady());

    HttpRequest request = parser.getHttpRequest();
    assertEquals("/user", request.getUri());
    assertArrayEquals("{\"name\":\"admin\"}".getBytes(), request.getBody());
    assertEquals("HTTP/1.1", request.getVersion());

    HttpHeaders httpHeaders = request.getHttpHeaders();
    assertEquals(3, httpHeaders.size());
    List<HttpHeader> headerList = httpHeaders.toSortedList();

    HttpHeader h1 = headerList.get(0);
    assertEquals("content-length", h1.getKey());
    assertEquals("16", h1.getValue());

    HttpHeader h2 = headerList.get(1);
    assertEquals("content-type", h2.getKey());
    assertEquals("application/json", h2.getValue());

    HttpHeader h3 = headerList.get(2);
    assertEquals("host", h3.getKey());
    assertEquals("www.example.com", h3.getValue());
  }

  @Test
  public void should_parse_delete_request_success() {
    HttpRequestParser parser = new HttpRequestParser();
    String r = "DELETE /user/1 HTTP/1.1\r\n"
      + "host: www.example.com\r\n"
      + "\r\n";
    ByteBuffer byteBuffer = ByteBuffer.wrap(r.getBytes(StandardCharsets.UTF_8));

    parser.read(byteBuffer);
    assertTrue(parser.isReady());

    HttpRequest request = parser.getHttpRequest();
    assertEquals("/user/1", request.getUri());
    assertNull(request.getBody());
    assertEquals("HTTP/1.1", request.getVersion());

    HttpHeaders httpHeaders = request.getHttpHeaders();
    assertEquals(1, httpHeaders.size());
    List<HttpHeader> headerList = httpHeaders.toSortedList();

    HttpHeader h3 = headerList.get(0);
    assertEquals("host", h3.getKey());
    assertEquals("www.example.com", h3.getValue());
  }
}

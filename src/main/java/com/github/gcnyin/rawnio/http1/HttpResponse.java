package com.github.gcnyin.rawnio.http1;

import com.github.gcnyin.rawnio.collection.ByteArray;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@ToString
public class HttpResponse {
  private final String version = "HTTP/1.1";
  private final String statusCode = "200";
  private final String reasonPhrase = "OK";
  private final HttpHeaders headers = new HttpHeaders();
  private ByteArray body;

  public void addHeader(String key, String value) {
    headers.add(key, value);
  }

  public byte[] getBytes() {
    headers.remove("content-length");
    if (body != null) {
      headers.add("content-length", String.valueOf(body.size()));
    }
    String h = headers.toSortedList().stream()
      .map(it -> it.getKey() + ": " + it.getValue())
      .collect(Collectors.joining("\r\n")) + "\r\n\r\n";
    ByteArray byteArray = new ByteArray();
    byteArray.add((version + " ").getBytes(StandardCharsets.US_ASCII));
    byteArray.add((statusCode + " ").getBytes(StandardCharsets.US_ASCII));
    byteArray.add((reasonPhrase + "\r\n").getBytes(StandardCharsets.US_ASCII));
    byteArray.add(h.getBytes(StandardCharsets.US_ASCII));
    if (body != null) {
      byteArray.add(body.getBytes());
    }
    byteArray.add("\r\n".getBytes(StandardCharsets.US_ASCII));
    return byteArray.getBytes();
  }

  public String getVersion() {
    return version;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public ByteArray getBody() {
    return body;
  }

  public void setBody(ByteArray body) {
    this.body = body;
  }
}

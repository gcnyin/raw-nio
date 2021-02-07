package com.github.gcnyin.rawnio.http1;

import com.github.gcnyin.rawnio.collection.ByteArray;
import com.github.gcnyin.rawnio.eventloop.SocketContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpContext {
  private final HttpRequest request;
  private final HttpResponse response;
  private final SocketContext socketContext;

  public HttpContext(HttpRequest request, HttpResponse response, SocketContext socketContext) {
    this.request = request;
    this.response = response;
    this.socketContext = socketContext;
  }

  public HttpContext status(String status) {
    response.setStatusCode(status);
    response.setReasonPhrase(toReason(status));
    return this;
  }

  private String toReason(String status) {
    switch (status) {
      case "200":
        return "OK";
      case "404":
        return "Not Found";
    }
    return "OK";
  }

  public void json(String s) throws IOException {
    response.getHeaders().remove("content-type");
    response.setBody(ByteArray.from(s.getBytes(StandardCharsets.UTF_8)));
    response.getHeaders().add("content-type", "application/json");
    byte[] bytes = response.getBytes();
    response.getHeaders().add("content-length", String.valueOf(bytes.length));
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    socketContext.getSocketChannel().write(buffer);
  }

  public HttpRequest getRequest() {
    return request;
  }

  public void close() throws IOException {
    socketContext.close();
  }
}

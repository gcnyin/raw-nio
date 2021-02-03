package com.github.gcnyin.rawnio.http1.handler;

import com.github.gcnyin.rawnio.http1.HttpContext;
import com.github.gcnyin.rawnio.http1.HttpRequest;

import java.io.IOException;

public class UriHandler implements HttpRequestHandler {
  @Override
  public void handle(HttpContext ctx) {
    HttpRequest request = ctx.getRequest();
    String uri = request.getUri();
    String body = "{\"uri\":\"" + uri + "\"}";
    try {
      ctx.status("200").json(body);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

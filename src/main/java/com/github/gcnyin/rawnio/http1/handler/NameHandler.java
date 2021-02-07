package com.github.gcnyin.rawnio.http1.handler;

import com.github.gcnyin.rawnio.http1.HttpContext;

import java.io.IOException;

public class NameHandler implements HttpRequestHandler {
  @Override
  public void handle(HttpContext ctx) {
    String s = new String(ctx.getRequest().getBody());
    if (s.contains("hhz")) {
      try {
        ctx.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
    try {
      ctx.json("{\"result\":\"ok\"}");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

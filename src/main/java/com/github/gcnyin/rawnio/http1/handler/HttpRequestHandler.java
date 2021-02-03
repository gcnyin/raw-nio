package com.github.gcnyin.rawnio.http1.handler;

import com.github.gcnyin.rawnio.http1.HttpContext;

public interface HttpRequestHandler {
  void handle(HttpContext ctx);
}

package com.github.gcnyin.rawnio.http1.handler;

import com.github.gcnyin.rawnio.http1.HttpRequest;
import com.github.gcnyin.rawnio.http1.HttpResponse;

public interface HttpRequestHandler {
  HttpResponse handle(HttpRequest request);
}

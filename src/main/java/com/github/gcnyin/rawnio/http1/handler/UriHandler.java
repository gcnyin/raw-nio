package com.github.gcnyin.rawnio.http1.handler;

import com.github.gcnyin.rawnio.collection.ByteArray;
import com.github.gcnyin.rawnio.http1.HttpRequest;
import com.github.gcnyin.rawnio.http1.HttpResponse;

public class UriHandler implements HttpRequestHandler {
  @Override
  public HttpResponse handle(HttpRequest request) {
    String uri = request.getUri();
    String body = "{\"uri\":\"" + uri + "\"}";
    HttpResponse response = new HttpResponse();
    response.setBody(ByteArray.from(body.getBytes()));
    response.addHeader("content-type", "application/json");
    return response;
  }
}

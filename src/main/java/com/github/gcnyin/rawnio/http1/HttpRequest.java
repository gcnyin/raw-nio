package com.github.gcnyin.rawnio.http1;

public class HttpRequest {
  private String version; // not null
  private HttpMethod httpMethod; // not null
  private final HttpHeaders httpHeaders = new HttpHeaders();
}

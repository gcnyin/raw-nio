package com.github.gcnyin.rawnio.http1;

import java.util.Arrays;

public class HttpRequest {
  private final HttpMethod httpMethod;
  private final String uri;
  private final String version;
  private final HttpHeaders httpHeaders;
  private final byte[] body;

  public HttpRequest(HttpMethod httpMethod, String uri, String version, HttpHeaders httpHeaders, byte[] body) {
    this.httpMethod = httpMethod;
    this.uri = uri;
    this.version = version;
    this.httpHeaders = httpHeaders;
    this.body = body;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getUri() {
    return uri;
  }

  public String getVersion() {
    return version;
  }

  public HttpHeaders getHttpHeaders() {
    return httpHeaders;
  }

  public byte[] getBody() {
    return body;
  }

  @Override
  public String toString() {
    return "HttpRequest{" +
      "httpMethod=" + httpMethod +
      ", uri='" + uri + '\'' +
      ", version='" + version + '\'' +
      ", httpHeaders=" + httpHeaders +
      ", body=" + Arrays.toString(body) +
      '}';
  }
}

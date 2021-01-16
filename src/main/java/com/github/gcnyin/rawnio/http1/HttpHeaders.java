package com.github.gcnyin.rawnio.http1;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
  private final Map<String, String> headers = new HashMap<>();

  public HttpHeaders add(String key, String value) {
    headers.put(key, value);
    return this;
  }

  public HttpHeaders remove(String key) {
    headers.remove(key);
    return this;
  }

  public String getValue(String key) {
    return headers.get(key);
  }
}

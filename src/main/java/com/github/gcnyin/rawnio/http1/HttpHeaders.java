package com.github.gcnyin.rawnio.http1;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
  private final Map<String, String> headers = new HashMap<>();

  public void add(String key, String value) {
    headers.put(key.toLowerCase(), value);
  }

  public void remove(String key) {
    headers.remove(key.toLowerCase());
  }

  public String get(String key) {
    return headers.get(key.toLowerCase());
  }

  public boolean containsKey(String key) {
    return headers.containsKey(key.toLowerCase());
  }
}

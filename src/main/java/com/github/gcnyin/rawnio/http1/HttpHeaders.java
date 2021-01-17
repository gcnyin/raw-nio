package com.github.gcnyin.rawnio.http1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeaders {
  private final Map<String, String> headers = new HashMap<>();

  public void add(String key, String value) {
    headers.put(key.toLowerCase(), value);
  }

  public void add(HttpHeader header) {
    headers.put(header.getKey().toLowerCase(), header.getValue());
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

  public List<HttpHeader> toList() {
    return headers.entrySet().stream()
      .map(it -> new HttpHeader(it.getKey(), it.getValue()))
      .collect(Collectors.toList());
  }

  public Map<String, String> toMap() {
    return new HashMap<>(headers);
  }
}

package com.github.gcnyin.rawnio.http1;

import lombok.ToString;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
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

  public int size() {
    return headers.size();
  }

  public List<HttpHeader> toSortedList() {
    return headers.entrySet().stream()
      .map(it -> new HttpHeader(it.getKey(), it.getValue()))
      .sorted(Comparator.comparing(HttpHeader::getKey))
      .collect(Collectors.toList());
  }
}

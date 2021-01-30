package com.github.gcnyin.rawnio.http1;

import java.util.LinkedList;
import java.util.List;

public class HttpHeaders {
  private final List<HttpHeader> headers = new LinkedList<>();

  public void add(String key, String value) {
    headers.add(new HttpHeader(key.toLowerCase(), value));
  }

  public void remove(String key) {
    headers.removeIf(it -> it.getKey().equals(key));
  }

  public String get(String key) {
    key = key.toLowerCase();
    for (HttpHeader header : headers) {
      if (header.getKey().equals(key)) {
        return header.getValue();
      }
    }
    return null;
  }

  public boolean containsKey(String key) {
    key = key.toLowerCase();
    for (HttpHeader header : headers) {
      if (header.getKey().equals(key)) {
        return true;
      }
    }
    return false;
  }

  public int size() {
    return headers.size();
  }

  public List<HttpHeader> getList() {
    return headers;
  }

  @Override
  public String toString() {
    return "HttpHeaders{" +
      "headers=" + headers +
      '}';
  }
}

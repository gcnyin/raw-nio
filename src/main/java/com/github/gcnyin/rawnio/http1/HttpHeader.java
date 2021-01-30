package com.github.gcnyin.rawnio.http1;

public class HttpHeader {
  private final String key;
  private final String value;

  public HttpHeader(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "HttpHeader{" +
      "key='" + key + '\'' +
      ", value='" + value + '\'' +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HttpHeader that = (HttpHeader) o;

    if (!key.equals(that.key)) return false;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + value.hashCode();
    return result;
  }
}

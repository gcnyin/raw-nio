package com.github.gcnyin.rawnio.http1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpHeader {
  private final String key;
  private final String value;
}

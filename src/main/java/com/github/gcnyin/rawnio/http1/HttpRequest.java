package com.github.gcnyin.rawnio.http1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequest {
  private HttpMethod httpMethod;
  private String uri;
  private String version;
  private HttpHeaders httpHeaders;
  private byte[] body;
}

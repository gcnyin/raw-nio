package com.github.gcnyin.rawnio.http1;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
public class HttpServerHandler implements SocketHandler {
  private final ByteBuffer buffer = ByteBuffer.allocate(1024);
  private final HttpRequestParser httpRequestParser = new HttpRequestParser();
  private final SocketContext ctx;

  public HttpServerHandler(SocketContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public void onRead() throws IOException {
    int i = ctx.getSocketChannel().read(buffer);
    if (i == -1) {
      ctx.getSocketChannel().close();
      log.info("ID: {}, connection closed", ctx.getConnectionId());
      return;
    }
    buffer.flip();
    httpRequestParser.read(buffer);
    buffer.clear();
    if (!httpRequestParser.isReady()) {
      return;
    }
    HttpRequest httpRequest = httpRequestParser.getHttpRequest();
    log.info("{}", httpRequest);
  }
}

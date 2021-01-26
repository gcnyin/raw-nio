package com.github.gcnyin.rawnio.http1;

import com.github.gcnyin.rawnio.eventloop.SocketContext;
import com.github.gcnyin.rawnio.eventloop.SocketHandler;
import com.github.gcnyin.rawnio.logging.Logger;
import com.github.gcnyin.rawnio.logging.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HttpServerHandler implements SocketHandler {
  private static final Logger log = LoggerFactory.getLogger(HttpServerHandler.class);

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
      log.info("ID: " + ctx.getConnectionId() + ", connection closed");
      return;
    }
    buffer.flip();
    httpRequestParser.read(buffer);
    buffer.clear();
    if (!httpRequestParser.isReady()) {
      return;
    }
    HttpRequest httpRequest = httpRequestParser.getHttpRequest();
    log.info(httpRequest.toString());
  }
}

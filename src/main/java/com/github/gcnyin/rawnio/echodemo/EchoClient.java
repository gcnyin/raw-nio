package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.ClientBootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EchoClient {
  public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
    ClientBootstrap clientBootstrap = new ClientBootstrap();
    clientBootstrap.setSocketHandlerProvider(EchoClientHandler::new);
    InetSocketAddress address = new InetSocketAddress("localhost", 8080);
    CompletableFuture<String> future = clientBootstrap.connect(address);
    future.get();
  }
}

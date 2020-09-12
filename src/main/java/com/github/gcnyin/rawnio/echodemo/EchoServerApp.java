package com.github.gcnyin.rawnio.echodemo;

import com.github.gcnyin.rawnio.eventloop.Bootstrap;

import java.io.IOException;

public class EchoServerApp {
  public static void main(String[] args) throws IOException {
    new Bootstrap(8080).start();
  }
}

package com.github.gcnyin.rawnio;

import com.github.gcnyin.rawnio.eventloop.Bootstrap;

import java.io.IOException;

public class App {
  public static void main(String[] args) throws IOException {
    new Bootstrap(8080).start();
  }
}

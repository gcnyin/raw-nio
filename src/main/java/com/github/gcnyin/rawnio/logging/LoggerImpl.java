package com.github.gcnyin.rawnio.logging;

import java.time.Instant;

public class LoggerImpl implements Logger {
  private final String name;

  public LoggerImpl(String name) {
    this.name = name;
  }

  @Override
  public void debug(String s) {
    print("debug", s);
  }

  @Override
  public void info(String s) {
    print("info", s);
  }

  @Override
  public void warning(String s) {
    print("warning", s);
  }

  @Override
  public void error(String s) {
    print("error", s);
  }

  @Override
  public void error(String s, Throwable t) {
    print("error", s);
    t.printStackTrace();
  }

  private void print(String level, String msg) {
    String threadName = Thread.currentThread().getName();
    String time = Instant.now().toString();
    String s = time + " " + level + " " + threadName + " " + name + " " + msg;
    System.out.println(s);
  }
}

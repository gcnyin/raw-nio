package com.github.gcnyin.rawnio.logging;

public class LoggerFactory {
  public static Logger getLogger(String name) {
    return new LoggerImpl(name);
  }

  public static Logger getLogger(Class<?> c) {
    return getLogger(c.getName());
  }
}

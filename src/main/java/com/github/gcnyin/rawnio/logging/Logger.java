package com.github.gcnyin.rawnio.logging;

public interface Logger {
  void debug(String s);

  void info(String s);

  void warning(String s);

  void error(String s);

  void error(String s, Throwable t);
}

package com.github.gcnyin.rawnio.loadbalancer.serverpool;

import com.github.gcnyin.rawnio.loadbalancer.Server;

public interface ServerPool {
  Server getServer();
}

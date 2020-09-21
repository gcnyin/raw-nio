# raw-nio

Raw Java NIO practice

## Requirements

- Java 11

## Event Loop

Here is my implementation of the event loop and several applications build-on the eventloop.

- echo
- load balancer
    - random
    - round robin
    - min connection count

## TCP Load Balancer

Example

```
java -cp build/libs/raw-nio-all.jar \
    com.github.gcnyin.rawnio.loadbalancer.LoadBalancer \
    8080 localhost:8081,localhost:8082
```

`8080` is the load balancer port. `localhost:8081,localhost:8082` is the backend server list.

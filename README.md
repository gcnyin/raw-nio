# raw-nio

Raw Java NIO practice

## Requirements

- Java 11

## Event Loop

Here is my implementation of the event loop. I have developed several applications with the implementation.

- echo
- load balancer

## Load Balancer

Example

```
java -cp build/libs/raw-nio-all.jar com.github.gcnyin.rawnio.loadbalancer.LoadBalancer 8080 localhost:8081,localhost:8082
```

`8080` is load balancer port. `localhost:8081,localhost:8082` is backend server list.

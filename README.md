# raw-nio

Network applications based on Java raw nio.

## Event Loop

Here is my implementation of the event loop and several applications build-on the event loop.

- echo
- load balancer
    - random
    - round robin
    - min connection count
- HTTP/1.1 server
    - to-do: chunk and trailer

I also implemented an object pool `ByteBufferPool` to reuse `DirectByteBuffer`.

## TCP Load Balancer

Example

```
java -cp build/libs/raw-nio-all.jar \
    com.github.gcnyin.rawnio.loadbalancer.LoadBalancer \
    8080 localhost:8081,localhost:8082 roundRobinPool
```

`8080` is the load balancer port. `localhost:8081,localhost:8082` is the backend server list, `roundRobinPool` is the load balancer strategy, also can be `minConnectionCountPool` and `randomPool`.

## HTTP/1.1 Server

Example

```
java -cp build/libs/raw-nio-all.jar \
    com.github.gcnyin.rawnio.http1.HttpServer 8080
```

`8080` is the HTTP/1.1 server port.

## Build

### Native image

Install [Graalvm](https://www.graalvm.org).

Install `native-image`

```
gu install native-image
```

Build jar

```
./gradlew jar
```

Build native image

```
native-image -jar ./build/libs/raw-nio.jar
```

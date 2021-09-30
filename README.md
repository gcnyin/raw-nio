# raw-nio

Network applications based on Java raw nio.

- Java 8 11 17

## Event Loop

This is my implementation of the event loop, and I built multiple applications on the event loop.

- echo
- TLV protocol(type-lenght-value)
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

### Jar

```
./gradlew clean build
```

### Native image

Install [Graalvm](https://www.graalvm.org).

Install `native-image`

```
gu install native-image
```

Build jar

```
./gradlew clean build
```

Build native image

```
native-image -jar ./build/libs/raw-nio.jar
```

### Jpackage

Need JDK 17.

```
cd build/libs

jpackage --input . --name raw-nio \
  --main-jar raw-nio.jar \
  --main-class com.github.gcnyin.rawnio.http1.HttpServer \
  --type dmg \
  --java-options '--enable-preview'
```

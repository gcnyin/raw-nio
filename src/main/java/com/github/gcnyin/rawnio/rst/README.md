# RST

打开tcpdump，网卡名称可能得换下。

```
sudo tcpdump -i lo0 src host localhost and port 6543
```

依次启动RstServer和RstClient。

## M-Rpc架构设计

### M-Rpc核心组件

#### Register Service

- 服务注册/下线
- 服务发现，提供服务元数据信息
- 心跳探活

#### Server端

- 服务端启动将服务发布到注册中心
- 收到客户端的数据，解码发起服务调用，最后将结果返回给客户端

#### Client端

- 从注册中心获取服务列表，服务的元数据信息
- 本地Proxy模块动态生成Stub（桩），将收到的参数转化为网络字节流

### MinRpc需要解决的问题

#### 服务注册与发现

#### 通信协议/序列化

通信协议的选择

- TCP，HTTP协议
- UDP协议

序列化框架的选择

- Kryo
- Protobuf
- Hessian

#### RPC调用

- 同步-Sync
- 异步-Future
- 回调-Callback
- 单向-OneWay

#### 线程模型

根据不同策略决定I/O线程和业务线程的使用情况

- I/O线程
- 业务线程

#### 负载均衡-LoadBalancer

注册中心获取到服务节点信息后，客户端可以根据不同策略进行负载均衡选取最佳可用节点

- Round-Robin 轮询，简单粗暴，不考虑服务端节点的实际负载水平
- Weighted Round-Robin 权重轮询，根据节点的服务质量给节点赋予不同的权重系数，轮询时根据权重因子可适当减少配置低的服务节点压力
- Least Connections 最少连接数，选择连接数最少的一台服务器
- Consistent Hash 一致性哈希，目前主流的负载均衡方案

#### 动态代理

客户端为了实现像调用本地方法一样调用远程服务，需要在本地动态代理，生成Stub桩，在Stub中完成数据编解码发起远程调用

- JDK动态代理
- Cglib
- Javaassist/ASM

### M-Rpc模块规划

- `mrpc-core`基础类库，工具类，实体类等
- `mrpc-nameserver`注册中心，提供服务注册与发现，心跳探活
- `mrpc-protocol`网络通信，RPC编解码器，序列化和反序列化

- `mrpc-server`服务端，提供RPC服务，接受处理客户端RPC请求
- `mrpc-api`服务端RPC暴露的服务接口
- `mrpc-client`客户端，动态代理向服务端发起RPC请求


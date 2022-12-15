package me.lattice.mrpc.client.boot;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.core.meta.MRpcRequest;
import me.lattice.mrpc.core.meta.ServiceMetadata;
import me.lattice.mrpc.core.util.MRpcServiceHelper;
import me.lattice.mrpc.protocol.codec.MRpcMessageDecoder;
import me.lattice.mrpc.protocol.codec.MRpcMessageEncoder;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;
import me.lattice.mrpc.protocol.transport.MRpcRequestHolder;
import me.lattice.mrpc.registry.core.MRpcRegistryService;

import java.util.Objects;

/**
 * @description: RPC客户端，启动Netty客户端，连接服务端，发送请求，接收响应，关闭连接
 * @author: lattice
 * @date: 2022/12/15 4:51 PM
 */
@Slf4j
public class MRpcClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    public MRpcClient() {
        // 初始化Netty客户端
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(6);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                .addLast(new MRpcMessageEncoder())
                                .addLast(new MRpcMessageDecoder())
                        ;
                    }
                });
    }

    public void sendRequest(MRpcRegistryService registryService, MRpcProtocol<MRpcRequest> protocol) throws Exception {
        MRpcRequest request = protocol.getBody();
        Object[] parameters = request.getParameters();
        String serviceName = MRpcServiceHelper.buildServiceName(request.getInterfaceName(), request.getVersion());
        int hashcode = parameters.length == 0 ? parameters[0].hashCode() : serviceName.hashCode();
        // 1. 从注册中心获取服务提供者地址
        ServiceMetadata metadata = registryService.discovery(serviceName, hashcode);
        if (Objects.nonNull(metadata)) {
            // 2. 通过Netty发送请求
            ChannelFuture future = bootstrap.connect(metadata.getServiceAddress(), metadata.getServicePort()).sync();
            // 3. 注册监听器
            future.addListener(e -> {
                if (future.isSuccess()) {
                    log.info("Client connect to server success, server address: {}, port: {}", metadata.getServiceAddress(), metadata.getServicePort());
                } else {
                    log.error("Client connect to server failed, server address: {}, port: {}", metadata.getServiceAddress(), metadata.getServicePort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(protocol);
        }
    }
}

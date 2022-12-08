package me.lattice.mrpc.webservice.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.core.constants.SymbolConstant;
import me.lattice.mrpc.webservice.annotation.MRpcService;
import me.lattice.mrpc.webservice.props.MRpcRegistryProperties;
import me.lattice.mrpc.webservice.props.MRpcServerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: MRpc Server
 * @author: lattice
 * @date: 2022/12/8 1:55 PM
 */
@Slf4j
public class MRpcServer implements InitializingBean, BeanPostProcessor {

    /** 服务暴露端口 **/
    private int serverPort;
    /** 注册服务可插拔式 **/
    private MRpcRegistryService registryService;
    /** 缓存所有服务Bean **/
    private Map<String, Object> serviceMap = new HashMap<>();

    /** 服务注册表 **/
    public MRpcServer(int serverPort, MRpcRegistryService registryService) {
        this.serverPort = serverPort;
        this.registryService = registryService;
    }

    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {
            try {
                startServer();
            } catch (Exception e) {
                log.error("start mRpc server error: {}", e.getMessage());
            }
        }).start();
    }


    /**
     * @description: 启动Netty Server
     * @date: 2022/12/8 2:00 PM
     */
    public void startServer() throws Exception {
        // start mRpc server
        // get local host
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        // create a netty server
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ch.pipeline().addLast(new MRpcServerHandler(serviceMap));
                        }
                    });
            Channel channel = serverBootstrap.bind(hostAddress, serverPort).sync().channel();
            log.info("mRpc server started on {}:{}", hostAddress, serverPort);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("mRpc server start error: {}", e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        MRpcService mRpcService = bean.getClass().getAnnotation(MRpcService.class);
        if (Objects.nonNull(mRpcService)) {
            try {
                String serviceName = mRpcService.serviceName().getName();
                String version = mRpcService.version();
                String serviceNameVersion = serviceName + SymbolConstant.DASH + version;
                // register service
                registryService.registerService();
                // cache service bean
                serviceMap.put(serviceNameVersion, bean);
                log.info("register service: {}", serviceNameVersion);
            } catch (Exception e) {
                log.error("register service error: {}", e.getMessage());
            }
        }
        return bean;
    }


}

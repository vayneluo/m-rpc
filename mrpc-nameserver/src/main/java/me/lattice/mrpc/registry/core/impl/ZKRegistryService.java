package me.lattice.mrpc.registry.core.impl;

import me.lattice.mrpc.core.constants.ZookeeperConstant;
import me.lattice.mrpc.core.meta.ServiceMetadata;
import me.lattice.mrpc.core.util.MRpcServiceHelper;
import me.lattice.mrpc.registry.core.MRpcRegistryService;
import me.lattice.mrpc.registry.loadbalance.impl.ZKConsistentHashLoadBalancer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;

/**
 * @description: zookeeper registry service for server to register service
 * @author: lattice
 * @date: 2022/12/15 9:49 AM
 */
public class ZKRegistryService implements MRpcRegistryService {

    private final ServiceDiscovery<ServiceMetadata> serviceDiscovery;

    public ZKRegistryService(String address) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(address,
                new ExponentialBackoffRetry(ZookeeperConstant.SLEEP_TIME, ZookeeperConstant.MAX_RETRIES));
        client.start();
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
                .client(client)
                .basePath(ZookeeperConstant.ROOT_PATH)
                .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void register(ServiceMetadata metadata) throws Exception {
        ServiceInstance<ServiceMetadata> serviceInstance = ServiceInstance.<ServiceMetadata>builder()
                .name(MRpcServiceHelper.buildServiceName(metadata.getServiceName(), metadata.getVersion()))
                .address(metadata.getServiceAddress())
                .port(metadata.getServicePort())
                .payload(metadata)
                .build();
        this.serviceDiscovery.registerService(serviceInstance);

    }

    @Override
    public void unregister(ServiceMetadata metadata) throws Exception {

    }

    @Override
    public ServiceMetadata discovery(String serviceName, int hashcode) throws Exception {
        Collection<ServiceInstance<ServiceMetadata>> serviceInstances = this.serviceDiscovery.queryForInstances(serviceName);
        // todo 1.后续可优化成指定负载均衡策略，默认走一致性哈希
        // todo 2.不区分注册中心，负载均衡的策略不因为注册中心的不同而不同
        ServiceInstance<ServiceMetadata> instance = new ZKConsistentHashLoadBalancer()
                .select((List<ServiceInstance<ServiceMetadata>>) serviceInstances, hashcode);
        return instance.getPayload();
    }

    @Override
    public void remove() throws Exception {

    }
}

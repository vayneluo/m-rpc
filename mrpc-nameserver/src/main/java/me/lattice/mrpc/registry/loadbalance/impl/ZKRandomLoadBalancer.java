package me.lattice.mrpc.registry.loadbalance.impl;

import me.lattice.mrpc.core.meta.ServiceMetadata;
import me.lattice.mrpc.registry.loadbalance.MRpcLoadBalancer;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Random;

/**
 * @description: 负载均衡-随机策略
 * @author: lattice
 * @date: 2022/12/21 4:59 PM
 */
public class ZKRandomLoadBalancer implements MRpcLoadBalancer<ServiceInstance<ServiceMetadata>> {

    private Random random;

    public ZKRandomLoadBalancer() {
        this.random = new Random();
    }

    @Override
    public ServiceInstance<ServiceMetadata> select(List<ServiceInstance<ServiceMetadata>> providers, int hashCode) {
        int index = random.nextInt(providers.size());
        return providers.get(index);
    }
}

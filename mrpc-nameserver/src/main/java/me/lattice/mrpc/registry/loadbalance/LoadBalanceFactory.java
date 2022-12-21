package me.lattice.mrpc.registry.loadbalance;

import me.lattice.mrpc.core.enums.LoadBalanceStrategy;
import me.lattice.mrpc.core.meta.ServiceMetadata;
import me.lattice.mrpc.registry.loadbalance.impl.ZKConsistentHashLoadBalancer;
import me.lattice.mrpc.registry.loadbalance.impl.ZKRandomLoadBalancer;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @description: 负载均衡策略工厂类
 * @author: lattice
 * @date: 2022/12/21 4:57 PM
 */
public class LoadBalanceFactory {

    public static MRpcLoadBalancer<ServiceInstance<ServiceMetadata>> getLoadBalancer(LoadBalanceStrategy strategy) {
        switch (strategy) {
            case RANDOM:
                return new ZKRandomLoadBalancer();
            case CONSISTENT_HASH:
                return new ZKConsistentHashLoadBalancer();
            default:
                return new ZKRandomLoadBalancer();
        }
    }

}

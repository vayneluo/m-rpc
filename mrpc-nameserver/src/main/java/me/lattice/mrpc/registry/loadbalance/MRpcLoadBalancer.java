package me.lattice.mrpc.registry.loadbalance;

import java.util.List;

/**
 * @description: MRpc Load Balancer For Service Consumer To Select Service Provider From Registry Center
 * @author: lattice
 * @date: 2022/12/15 11:13 AM
 */
public interface MRpcLoadBalancer<T> {

    /**
     * Select Service Provider From Registry Center
     * @param providers Service Provider List
     * @param hashCode Hash Code
     * @return Service Provider
     */
    T select(List<T> providers, int hashCode);
}

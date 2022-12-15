package me.lattice.mrpc.registry.loadbalance.impl;

import me.lattice.mrpc.core.constants.SymbolConstant;
import me.lattice.mrpc.core.meta.ServiceMetadata;
import me.lattice.mrpc.registry.loadbalance.MRpcLoadBalancer;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.TreeMap;

/**
 * @description: zookeeper consistent hash load balancer for service consumer to select service provider from registry center
 * @author: lattice
 * @date: 2022/12/15 11:17 AM
 */
public class ZKConsistentHashLoadBalancer implements MRpcLoadBalancer<ServiceInstance<ServiceMetadata>> {

    /** 虚拟服务节点个数 方便哈希均匀分布**/
    private static final int VIRTUAL_NODE_NUM = 10;


    /**
     * Select Service Provider From Registry Center
     * @param providers Service Provider List
     * @param hashCode Hash Code
     * @return 服务实例
     */
    @Override
    public ServiceInstance<ServiceMetadata> select(List<ServiceInstance<ServiceMetadata>> providers, int hashCode) {
        // 构造哈希环
        TreeMap<Integer, ServiceInstance<ServiceMetadata>> ring = buildConsistentHashRing(providers);
        return selectNode(ring, hashCode);
    }

    /**
     * 分配服务节点
     * @param ring 哈希环
     * @param hashCode Hash Code
     * @return 服务实例
     */
    private ServiceInstance<ServiceMetadata> selectNode(TreeMap<Integer, ServiceInstance<ServiceMetadata>> ring, int hashCode) {
        Integer key = ring.ceilingKey(hashCode);
        if (key == null) {
            key = ring.firstKey();
        }
        return ring.get(key);
    }

    /**
     * Build Consistent Hash Ring
     * @param providers 服务提供者列表
     * @return 哈希环
     */
    private TreeMap<Integer, ServiceInstance<ServiceMetadata>> buildConsistentHashRing(List<ServiceInstance<ServiceMetadata>> providers) {
        TreeMap<Integer, ServiceInstance<ServiceMetadata>> ringMap = new TreeMap<>();
        providers.forEach(provider -> {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                ringMap.put(getHash(provider, i), provider);
            }
        });
        return ringMap;
    }

    /**
     * Get Hash Code eg address:port#虚拟节点编号
     *
     * @param instance 服务实例
     * @param virtualNodeNum 虚拟节点编号
     * @return Hash Code
     */
    private int getHash(ServiceInstance<ServiceMetadata> instance, int virtualNodeNum) {
        ServiceMetadata payload = instance.getPayload();
        String addressPort = String.join(SymbolConstant.COLON, payload.getServiceAddress(),
                String.valueOf(payload.getServicePort()));
        // 最终key的格式 address:port#虚拟节点编号
        String key = addressPort + SymbolConstant.HYPHEN + virtualNodeNum;
        return key.hashCode();
    }
}

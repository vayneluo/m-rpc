package me.lattice.mrpc.registry.core;

import me.lattice.mrpc.core.meta.ServiceMetadata;

/**
 * @description: MRpc Registry Service For Server To Register Service
 * @author: lattice
 * @date: 2022/12/8 5:20 PM
 */
public interface MRpcRegistryService {

     void register(ServiceMetadata metadata) throws Exception;

     void unregister(ServiceMetadata metadata) throws Exception;

     ServiceMetadata discovery(String serviceName, int hashcode) throws Exception;

     void remove() throws Exception;
}

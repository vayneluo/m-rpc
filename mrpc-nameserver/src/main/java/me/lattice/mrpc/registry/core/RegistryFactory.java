package me.lattice.mrpc.registry.core;


import me.lattice.mrpc.core.enums.RegistryTypeEnum;
import me.lattice.mrpc.registry.core.impl.ZKRegistryService;

/**
 * @description: create registry service instance
 * @author: lattice
 * @date: 2022/12/8 5:22 PM
 */
public class RegistryFactory {

    private static volatile MRpcRegistryService registryService;

    /**
     * create registry service instance
     * @param registryTypeEnum 注册中心类型 eg: zookeeper / nacos / eureka
     * @param address 注册中心地址 eg: 127.0.0.1:2181
     * @return
     */
    public static MRpcRegistryService createRegistryService(RegistryTypeEnum registryTypeEnum, String address) throws Exception {
        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (registryTypeEnum) {
                        case ZOOKEEPER:
                            registryService = new ZKRegistryService(address);
                            break;
                        case NACOS:
                            // registryService = new NacosRegistryService(address);
                            break;
                        default:
                            throw new RuntimeException("registry type is not supported");
                    }
                }
            }
        }
        return registryService;
    }
}

package me.lattice.mrpc.webservice.core;

import me.lattice.mrpc.core.enums.RegistryTypeEnum;
import me.lattice.mrpc.registry.core.MRpcRegistryService;
import me.lattice.mrpc.registry.core.RegistryFactory;
import me.lattice.mrpc.webservice.props.MRpcRegistryProperties;
import me.lattice.mrpc.webservice.props.MRpcServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: MRpc自动注入配置类，用于配置MRpc服务端的相关信息
 * @author: lattice
 * @date: 2022/12/8 5:37 PM
 */
@Configuration
@EnableConfigurationProperties({MRpcServerProperties.class, MRpcRegistryService.class})
public class MRpcServerConfiguration {

    @Resource
    private MRpcServerProperties serverProperties;
    @Resource
    private MRpcRegistryProperties registryProperties;

    @Bean
    public MRpcServer mRpcServer() throws Exception {
        RegistryTypeEnum registryTypeEnum = RegistryTypeEnum.lookUp(registryProperties.getType());
        MRpcRegistryService registryService = RegistryFactory.createRegistryService(registryTypeEnum, registryProperties.getAddress());
        return new MRpcServer(serverProperties.getServerPort(), registryService);
    }

}

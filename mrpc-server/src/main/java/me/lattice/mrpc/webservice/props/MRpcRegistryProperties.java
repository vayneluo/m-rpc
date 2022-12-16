package me.lattice.mrpc.webservice.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: MRpc Registry Properties For Server And Client
 * @author: lattice
 * @date: 2022/12/8 1:41 PM
 */
@Data
@ConfigurationProperties(prefix = "mrpc.registry")
public class MRpcRegistryProperties {

    /** 注册中心类型 **/
    private String type;

    /** 注册中心地址 **/
    private String address;

}

package me.lattice.mrpc.webservice.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: MRpc Properties For Server And Client
 * @author: lattice
 * @date: 2022/12/8 12:53 PM
 */
@Data
@ConfigurationProperties(prefix = "mrpc.server")
public class MRpcServerProperties {

    /** 服务暴露端口 **/
    private int port;



}

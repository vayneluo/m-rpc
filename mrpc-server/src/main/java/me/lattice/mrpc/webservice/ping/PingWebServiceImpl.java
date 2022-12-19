package me.lattice.mrpc.webservice.ping;

import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.api.ping.IPingWebService;
import me.lattice.mrpc.webservice.annotation.MRpcService;

/**
 * @description: Ping Web Service Implementation For Client To Ping Server
 * @author: lattice
 * @date: 2022/12/7 5:30 PM
 */
@Slf4j
@MRpcService(serviceName = IPingWebService.class)
public class PingWebServiceImpl implements IPingWebService {

    @Override
    public String ping(String message) {
        log.info("receive ping message: {}", message);
        return "Ping success, Pong: " + message;
    }
}


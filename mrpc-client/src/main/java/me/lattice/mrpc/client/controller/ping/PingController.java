package me.lattice.mrpc.client.controller.ping;

import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.api.ping.IPingWebService;
import me.lattice.mrpc.core.annotation.MRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: Ping Controller For Client To Ping Server
 * @author: lattice
 * @date: 2022/12/7 5:43 PM
 */
@Slf4j
@RestController
public class PingController {

    @MRpcReference
    private IPingWebService pingWebService;

    @GetMapping("/ping/{message}")
    public String ping(@PathVariable String message) {
        log.info("send ping message: {}", message);
        return pingWebService.ping(message);
    }
}

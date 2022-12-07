package me.lattice.mrpc.api.ping;

/**
 * @description: Ping Web Service Interface For Client To Ping Server
 * @author: lattice
 * @date: 2022/12/7 5:27 PM
 */
public interface IPingWebService {

    /**
     * Ping message to pong
     * @param message message
     * @return pong message
     */
    String ping(String message);
}

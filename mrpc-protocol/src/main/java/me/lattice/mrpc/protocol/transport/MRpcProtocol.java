package me.lattice.mrpc.protocol.transport;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: MRpc Custom Protocol
 * @author: lattice
 * @date: 2022/12/10 8:48
 */
@Data
public class MRpcProtocol<T> implements Serializable {

    /**
     * MRpc Protocol Header 头部：魔数、版本、序列化方式、消息类型、状态、请求ID，数据长度
     */
    private MRpcHeader header;

    /**
     * MRpc Protocol Body 报文数据
     */
    private T body;

}

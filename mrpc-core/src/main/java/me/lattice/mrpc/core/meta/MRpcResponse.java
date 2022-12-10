package me.lattice.mrpc.core.meta;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: MRpc Response
 * @author: lattice
 * @date: 2022/12/10 13:09
 */
@Data
public class MRpcResponse implements Serializable {

    /**
     * 请求ID
     */
    private long requestId;

    /**
     * 响应结果
     */
    private Object data;

    /**
     * 异常
     */
    private String message;

}

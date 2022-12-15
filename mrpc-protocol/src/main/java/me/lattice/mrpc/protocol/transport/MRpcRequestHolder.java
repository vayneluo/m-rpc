package me.lattice.mrpc.protocol.transport;

import me.lattice.mrpc.core.meta.MRpcFuture;
import me.lattice.mrpc.core.meta.MRpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description: 请求持有类，用于存储请求信息，用于异步调用时，将请求信息存储起来，等待响应时，将请求信息取出来，进行回调
 * @author: lattice
 * @date: 2022/12/15 4:38 PM
 */
public class MRpcRequestHolder {

    public static AtomicLong GEN_REQ_ID = new AtomicLong(0);


    public static Map<Long, MRpcFuture<MRpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();

}

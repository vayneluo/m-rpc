package me.lattice.mrpc.client.bean;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import me.lattice.mrpc.client.boot.MRpcClient;
import me.lattice.mrpc.core.constants.MRpcProtocolConstant;
import me.lattice.mrpc.core.enums.MessageTypeEnum;
import me.lattice.mrpc.core.enums.ProtocolEnum;
import me.lattice.mrpc.core.enums.SerializationEnum;
import me.lattice.mrpc.core.meta.MRpcFuture;
import me.lattice.mrpc.core.meta.MRpcRequest;
import me.lattice.mrpc.core.meta.MRpcResponse;
import me.lattice.mrpc.protocol.transport.MRpcHeader;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;
import me.lattice.mrpc.protocol.transport.MRpcRequestHolder;
import me.lattice.mrpc.registry.core.MRpcRegistryService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @description: 核心代理类，执行RPC请求，解析RPC响应，返回结果
 * @author: lattice
 * @date: 2022/12/15 3:58 PM
 */
public class MRpcInvocationHandler implements InvocationHandler {

    private final MRpcRegistryService registryService;

    private final String version;

    private final long readTimeout;

    private final long connectTimeout;

    public MRpcInvocationHandler(MRpcRegistryService registryService, String version, long readTimeout, long connectTimeout) {
        this.registryService = registryService;
        this.version = version;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 0. 生成请求ID
        long reqId = MRpcRequestHolder.GEN_REQ_ID.incrementAndGet();
        // 1. 构建请求头
        MRpcProtocol<MRpcRequest> request = new MRpcProtocol<>();
        MRpcHeader header = new MRpcHeader();
        header.setMagicNumber(MRpcProtocolConstant.MAGIC_NUMBER);
        header.setMessageType(MessageTypeEnum.REQUEST.getCode());
        header.setSerializationType((byte) SerializationEnum.HESSIAN.getSerializeType());
        header.setStatus((byte) 0x1);
        header.setVersion(MRpcProtocolConstant.VERSION);
        header.setRequestId(reqId);
        request.setHeader(header);
        // 2. 构建请求体
        MRpcRequest body = new MRpcRequest();
        body.setInterfaceName(method.getDeclaringClass().getName());
        body.setVersion(version);
        body.setMethodName(method.getName());
        body.setParameters(args);
        body.setParameterTypes(method.getParameterTypes());
        request.setBody(body);

        // 3. 发送请求
        MRpcClient client = new MRpcClient();
        MRpcFuture<MRpcResponse> future = new MRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), readTimeout);
        MRpcRequestHolder.REQUEST_MAP.put(reqId, future);
        client.sendRequest(registryService, request);
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}

package me.lattice.mrpc.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.core.enums.MessageStatusEnum;
import me.lattice.mrpc.core.enums.MessageTypeEnum;
import me.lattice.mrpc.core.meta.MRpcRequest;
import me.lattice.mrpc.core.meta.MRpcResponse;
import me.lattice.mrpc.core.util.MRpcServiceHelper;
import me.lattice.mrpc.protocol.transport.MRpcHeader;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @description: MRpc Request Handler/ Inbound处理器
 * @author: lattice
 * @date: 2022/12/12 20:38
 */
@Slf4j
public class MRpcRequestHandler extends SimpleChannelInboundHandler<MRpcProtocol<MRpcRequest>> {

    /** 服务启动后一次性加载固定的服务列表，可直接取缓存里的 **/
    private final Map<String, Object> serviceMap;

    public MRpcRequestHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MRpcProtocol<MRpcRequest> msg) throws Exception {

        CustomThreadExecutor.execute(() -> {

            MRpcProtocol<MRpcResponse> responseProtocol = new MRpcProtocol<>();
            MRpcResponse response = new MRpcResponse();
            MRpcHeader header = msg.getHeader();
            header.setMessageType(MessageTypeEnum.RESPONSE.getCode());
            MRpcRequest request = msg.getBody();
            log.info("Receive request: {}", request);
            try {
                // RPC服务调用
                Object result = handle(request);
                response.setData(result);

                header.setStatus(MessageStatusEnum.SUCCESS.getStatus());

                responseProtocol.setHeader(header);
                responseProtocol.setBody(response);

            }catch (Throwable throwable){
                header.setStatus(MessageStatusEnum.FAIL.getStatus());
                response.setMessage(throwable.getMessage());
                log.error("RPC Server handle request error", throwable);
            }
        });
        ctx.writeAndFlush(msg);
    }

    /**
     * 反射方式执行目标方法
     * @param request 请求对象
     * @return 执行结果
     */
    private Object handle(MRpcRequest request) {
        String serviceName = MRpcServiceHelper.buildServiceName(request.getInterfaceName(), request.getVersion());
        Object instance = serviceMap.get(serviceName);
        if (Objects.isNull(instance)) {
            throw new RuntimeException(String.format("Can not find service instance by key: %s", serviceName));
        }
        Method method = ReflectionUtils.findMethod(instance.getClass(), request.getMethodName(), request.getParameterTypes());
        if (Objects.isNull(method)) {
            throw new RuntimeException(String.format("Can not find method by name: %s and parameter types: %s", request.getMethodName(), Arrays.toString(request.getParameterTypes())));
        }
        // 反射调用方法，获取执行结果
        return ReflectionUtils.invokeMethod(method, instance, request.getParameters());
    }

    /**
     * 通过CGLib调用目标对象的方法，非反射方式，加速性能
     * @param request 请求对象
     * @return 执行结果
     */
    private Object handleCglib(MRpcRequest request) throws InvocationTargetException {
        String serviceName = MRpcServiceHelper.buildServiceName(request.getInterfaceName(), request.getVersion());
        Object instance = serviceMap.get(serviceName);
        if (Objects.isNull(instance)) {
            throw new RuntimeException(String.format("Can not find service instance by key: %s", serviceName));
        }
        FastClass fastClass = FastClass.create(instance.getClass());
        int methodIndex = fastClass.getIndex(request.getMethodName(), request.getParameterTypes());
        return fastClass.invoke(methodIndex, request.getParameterTypes(), request.getParameters());
    }
}

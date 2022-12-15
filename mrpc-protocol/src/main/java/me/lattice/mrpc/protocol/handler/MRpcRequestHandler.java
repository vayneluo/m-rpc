package me.lattice.mrpc.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.core.enums.MessageStatusEnum;
import me.lattice.mrpc.core.enums.MessageTypeEnum;
import me.lattice.mrpc.core.meta.MRpcRequest;
import me.lattice.mrpc.core.meta.MRpcResponse;
import me.lattice.mrpc.protocol.transport.MRpcHeader;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;

/**
 * @description: MRpc Request Handler/ Inbound处理器
 * @author: lattice
 * @date: 2022/12/12 20:38
 */
@Slf4j
public class MRpcRequestHandler extends SimpleChannelInboundHandler<MRpcProtocol<MRpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MRpcProtocol<MRpcRequest> msg) throws Exception {

        CustomThreadExecutor.execute(() -> {

            MRpcProtocol<MRpcResponse> responseProtocol = new MRpcProtocol<>();
            MRpcResponse response = new MRpcResponse();
            MRpcHeader header = msg.getHeader();
            header.setMessageType(MessageTypeEnum.RESPONSE.getCode());
            MRpcRequest request = msg.getBody();
            try {
                // RPC服务调用
                Object result = handle(request);
                response.setRequestId(request.getRequestId());
                response.setData(result);

                header.setStatus(MessageStatusEnum.SUCCESS.getStatus());

                responseProtocol.setHeader(header);
                responseProtocol.setBody(response);

            }catch (Throwable throwable){
                header.setStatus(MessageStatusEnum.FAIL.getStatus());
                response.setMessage(throwable.getMessage());
                log.error("RPC Server handle request error", throwable);
            }
            log.info("Receive request: {}", request);
        });
        ctx.writeAndFlush(msg);
    }

    private Object handle(MRpcRequest request) {
        return null;
    }
}

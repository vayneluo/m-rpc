package me.lattice.mrpc.protocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.core.meta.MRpcFuture;
import me.lattice.mrpc.core.meta.MRpcResponse;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;
import me.lattice.mrpc.protocol.transport.MRpcRequestHolder;

/**
 * @description: MRpc响应结果处理 client端使用
 * @author: lattice
 * @date: 2022/12/13 20:45
 */
@Slf4j
public class MRpcResponseHandler extends SimpleChannelInboundHandler<MRpcProtocol<MRpcResponse>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MRpcProtocol<MRpcResponse> msg) throws Exception {
        log.info("Receive response: {}", msg.getBody());
        long requestId = msg.getHeader().getRequestId();
        MRpcResponse response = msg.getBody();
        MRpcFuture<MRpcResponse> future = MRpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(response);
    }
}

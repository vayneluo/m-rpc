package me.lattice.mrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.lattice.mrpc.core.constants.MRpcProtocolConstant;
import me.lattice.mrpc.core.enums.MessageTypeEnum;
import me.lattice.mrpc.core.meta.MRpcRequest;
import me.lattice.mrpc.core.meta.MRpcResponse;
import me.lattice.mrpc.protocol.serialization.MRpcSerialization;
import me.lattice.mrpc.protocol.serialization.SerializationFactory;
import me.lattice.mrpc.protocol.transport.MRpcHeader;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;

import java.util.List;

/**
 * @description: MRpc Request Decoder
 * @author: lattice
 * @date: 2022/12/10 18:59
 */
public class MRpcMessageDecoder extends ByteToMessageDecoder {

    /**
     * Inbound 入站解码操作
     * @param channelHandlerContext
     * @param byteBuf
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 请求字节数小于协议头部长度，直接返回
        if (byteBuf.readableBytes() < MRpcProtocolConstant.PROTOCOL_HEADER_TOTAL_LENGTH) {
            return;
        }
        // 标记当前读取位置
        byteBuf.markReaderIndex();
        // 魔数
        short magicNumber = byteBuf.readShort();
        if (magicNumber != MRpcProtocolConstant.MAGIC_NUMBER) {
            throw new IllegalArgumentException("Magic number is not correct, " + magicNumber);
        }
        // 版本号
        byte version = byteBuf.readByte();
        // 序列化方式
        byte serializationType = byteBuf.readByte();
        // 消息类型
        byte messageType = byteBuf.readByte();
        // 状态
        byte status = byteBuf.readByte();
        // 请求ID
        long requestId = byteBuf.readLong();
        // 数据长度
        int dataLength = byteBuf.readInt();
        // 请求字节数小于协议体长度，直接返回
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        // 数据
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        MRpcHeader header = new MRpcHeader();
        header.setMagicNumber(magicNumber);
        header.setVersion(version);
        header.setSerializationType(serializationType);
        header.setMessageType(messageType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setDataLength(dataLength);
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getMessageTypeEnum(messageType);
        // 序列化方式
        MRpcSerialization serialization = SerializationFactory.getSerialization(serializationType);
        switch (messageTypeEnum) {
            case REQUEST:
                MRpcProtocol<MRpcRequest> requestProtocol = new MRpcProtocol<>();
                requestProtocol.setHeader(header);
                requestProtocol.setBody(serialization.deserialize(data, MRpcRequest.class));
                out.add(requestProtocol);
                break;
            case RESPONSE:
                MRpcProtocol<MRpcResponse> responseProtocol = new MRpcProtocol<>();
                responseProtocol.setHeader(header);
                responseProtocol.setBody(serialization.deserialize(data, MRpcResponse.class));
                out.add(responseProtocol);
                break;
            default:
                throw new IllegalArgumentException("Message type is not correct, " + messageType);
        }
    }
}

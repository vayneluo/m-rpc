package me.lattice.mrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.lattice.mrpc.core.constants.MRpcProtocolConstant;
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
        if (byteBuf.readableBytes() < MRpcProtocolConstant.PROTOCOL_TOTAL_LENGTH) {
            return;
        }
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
        // 数据
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        // 封装成协议对象
        MRpcProtocol<Object> mRpcProtocol = new MRpcProtocol<>();
        MRpcHeader header = new MRpcHeader();
        header.setMagicNumber(magicNumber);
        header.setVersion(version);
        header.setSerializationType(serializationType);
        header.setMessageType(messageType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setDataLength(dataLength);
        mRpcProtocol.setHeader(header);
        mRpcProtocol.setBody(data);
        out.add(mRpcProtocol);

    }
}

package me.lattice.mrpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.lattice.mrpc.protocol.serialization.MRpcSerialization;
import me.lattice.mrpc.protocol.serialization.SerializationFactory;
import me.lattice.mrpc.protocol.transport.MRpcHeader;
import me.lattice.mrpc.protocol.transport.MRpcProtocol;

/**
 * @description: MRpc Request Encoder
 * @author: lattice
 * @date: 2022/12/10 18:58
 */
public class MRpcMessageEncoder extends MessageToByteEncoder<MRpcProtocol<Object>> {

    /**
     * Outbound 出站编码操作
     * @param channelHandlerContext
     * @param mRpcProtocol
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MRpcProtocol mRpcProtocol, ByteBuf byteBuf) throws Exception {
        MRpcHeader header = mRpcProtocol.getHeader();
        byte serializationType = header.getSerializationType();
        MRpcSerialization serialization = SerializationFactory.getSerialization(serializationType);
        // 自定义协议头
        // 魔数
        byteBuf.writeShort(header.getMagicNumber());
        // 版本号
        byteBuf.writeByte(header.getVersion());
        // 序列化方式
        byteBuf.writeByte(header.getSerializationType());
        // 消息类型
        byteBuf.writeByte(header.getMessageType());
        // 状态
        byteBuf.writeByte(header.getStatus());
        // 请求ID
        byteBuf.writeLong(header.getRequestId());
        // 数据长度
        byteBuf.writeInt(header.getDataLength());
        // 自定义协议体
        byteBuf.writeBytes(serialization.serialize(mRpcProtocol.getBody()));
    }
}

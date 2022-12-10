package me.lattice.mrpc.protocol.transport;

import lombok.Data;

/**
 * @description: MRpc Protocol Header
 * @author: lattice
 * @date: 2022/12/10 8:50
 */
@Data
public class MRpcHeader {

    /**
     * 协议组成
     * +---------------------------------------------------------------+
     * +
     * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
     * +
     * +---------------------------------------------------------------+
     * +
     * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
     * +
     * +---------------------------------------------------------------+
     * +
     * |                   数据内容 （长度不定）                          |
     * +
     * +---------------------------------------------------------------+
     */

    /**
     * MRpc Protocol Header Magic Number 魔数 2byte
     */
    private short magicNumber;

    /**
     * MRpc Protocol Header Version 版本 1byte
     */
    private byte version;

    /**
     * MRpc Protocol Header Serialization Type 序列化方式 1byte
     */
    private byte serializationType;

    /**
     * MRpc Protocol Header Message Type 消息类型 1byte
     */
    private byte messageType;

    /**
     * MRpc Protocol Header Status 状态 1byte
     */
    private byte status;

    /**
     * MRpc Protocol Header Request ID 请求ID 8byte
     */
    private long requestId;

    /**
     * MRpc Protocol Header Data Length 数据长度 4byte
     */
    private int dataLength;


}

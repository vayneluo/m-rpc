package me.lattice.mrpc.core.enums;

/**
 * @description: 消息类型枚举类
 * @author: lattice
 * @date: 2022/12/10 19:12
 */
public enum MessageTypeEnum {

    /**
     * 请求消息
     */
    REQUEST((byte) 0x01),

    /**
     * 响应消息
     */
    RESPONSE((byte) 0x02),

    /**
     * 一对一请求消息
     */
    ONE_WAY((byte) 0x03),

    /**
     * 心跳请求消息
     */
    HEARTBEAT_REQUEST((byte) 0x04),

    /**
     * 心跳响应消息
     */
    HEARTBEAT_RESPONSE((byte) 0x05);

    private byte code;

    MessageTypeEnum(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public static MessageTypeEnum getMessageTypeEnum(byte code) {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            if (messageTypeEnum.getCode() == code) {
                return messageTypeEnum;
            }
        }
        throw new IllegalArgumentException("不支持的消息类型: "+ code);
    }
}

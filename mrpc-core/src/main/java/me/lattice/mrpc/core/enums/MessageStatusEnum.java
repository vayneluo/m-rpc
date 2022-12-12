package me.lattice.mrpc.core.enums;

/**
 * @description: 消息状态枚举类
 */
public enum MessageStatusEnum {

    /**
     * 成功
     */
    SUCCESS((byte) 0x01),

    /**
     * 失败
     */
    FAIL((byte) 0x02);


    byte status;

    MessageStatusEnum(byte status) {
        this.status = status;
    }

    public byte getStatus() {
        return status;
    }

    public static MessageStatusEnum getMessageStatusEnum(byte status) {
        for (MessageStatusEnum messageStatusEnum : MessageStatusEnum.values()) {
            if (messageStatusEnum.getStatus() == status) {
                return messageStatusEnum;
            }
        }
        throw new IllegalArgumentException("不支持的消息状态: "+ status);
    }
}

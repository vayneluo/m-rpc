package me.lattice.mrpc.core.enums;

import lombok.Getter;

/**
 * @description: MRpc Serialize Enum
 * @author lattice
 */
public enum SerializationEnum {
    HESSIAN(0x01),
    JSON(0x02),
    KRYO(0x03),
    PROTOBUF(0x04),
    ;

    @Getter
    private final int serializeType;

    SerializationEnum(int serializeType) {
        this.serializeType = serializeType;
    }


    public static SerializationEnum getSerializationEnum(byte serializeType) {
        for (SerializationEnum serializationEnum : SerializationEnum.values()) {
            if (serializationEnum.getSerializeType() == serializeType) {
                return serializationEnum;
            }
        }
        throw new IllegalArgumentException("不支持的序列化方式: "+ serializeType);
    }
}

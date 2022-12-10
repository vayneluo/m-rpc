package me.lattice.mrpc.protocol.serialization;

import me.lattice.mrpc.core.enums.SerializationEnum;

/**
 * @description: 序列化工厂类，根据序列化方式获取序列化实例
 * @author: lattice
 * @date: 2022/12/10 18:02
 */
public class SerializationFactory {

        /**
         * 根据序列化类型获取序列化实例
         * @param serializationType 序列化类型
         * @return 序列化实例
         */
        public static MRpcSerialization getSerialization(byte serializationType) {
            SerializationEnum serializationEnum = SerializationEnum.getSerializationEnum(serializationType);
            switch (serializationEnum) {
                case JSON:
                    return new JsonSerialization();
                case HESSIAN:
                    return new HessianSerialization();
                default:
                    throw new IllegalArgumentException("不支持的序列化方式");
            }
        }

}

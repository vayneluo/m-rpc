package me.lattice.mrpc.protocol.serialization;

import java.io.IOException;

/**
 * @description: 通用序列化接口，支持多种序列化方式，如：JSON、Kryo、Protobuf、Hessian等
 */
public interface MRpcSerialization {

        /**
        * 序列化
        * @param obj 待序列化对象
        * @return
        */
        <T> byte[] serialize(T obj) throws IOException;

        /**
        * 反序列化
        * @param data 字节数组
        * @param clazz 反序列化的类型
        * @param <T> 反序列化的泛型
        * @return
        */
        <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}

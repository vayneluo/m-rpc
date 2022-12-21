package me.lattice.mrpc.protocol.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: Hessian序列化实现
 * @author: lattice
 * @date: 2022/12/10 18:09
 */
public class HessianSerialization implements MRpcSerialization {


    @Override
    public <T> byte[] serialize(T obj) {
        if (null == obj) {
            throw new IllegalArgumentException("序列化对象不能为空");
        }
        byte[] result;
        HessianSerializerOutput output;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            output = new HessianSerializerOutput(outputStream);
            output.writeObject(obj);
            output.flush();
            result = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (null == data || data.length == 0) {
            throw new NullPointerException();
        }
        T result;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
            HessianSerializerInput input = new HessianSerializerInput(inputStream);
            result = (T) input.readObject(clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

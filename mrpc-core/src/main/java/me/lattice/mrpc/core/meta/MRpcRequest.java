package me.lattice.mrpc.core.meta;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: MRpc Request
 * @author: lattice
 * @date: 2022/12/10 13:07
 */
@Data
public class MRpcRequest implements Serializable {

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] parameters;

    /**
     * 版本号
     */
    private String version;


}

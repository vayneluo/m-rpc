package me.lattice.mrpc.client.bean;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @description: 代理对象 Bean
 * @author: lattice
 * @date: 2022/12/9 4:25 PM
 */
@Data
public class MRpcProxyBean implements FactoryBean<Object> {

    /** 服务暴露接口 **/
    private Class<?> interfaceClass;

    /** 注册中心类型 **/
    private String registryType;

    /** 注册中心地址 **/
    private String registryAddress;

    /** 服务版本 **/
    private String version;

    /** 读取超时时间 **/
    private long readTimeout;

    /** 连接超时时间 **/
    private long connectTimeout;

    /** 代理对象 **/
    private Object proxyObject;

    public void init() throws Exception{
        // todo 动态代理创建代理对象
        // proxyObject = MRpcProxyFactory.createProxy(interfaceClass, registryType, registryAddress, version, readTimeout, connectTimeout);
    }

    @Override
    public Object getObject() throws Exception {
        return proxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}

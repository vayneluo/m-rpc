package me.lattice.mrpc.core.meta;

/**
 * @description: MRpc Metadata For Service Provider And Consumer To Exchange Information
 * @author: lattice
 * @date: 2022/12/8 4:18 PM
 */
public class ServiceMetadata {

    /** Service Name **/
    private String serviceAddress;

    /** Service Port **/
    private String servicePort;

    /** Service Name **/
    private String serviceName;

    /** Service Version **/
    private String version;

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

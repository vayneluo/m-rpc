package me.lattice.mrpc.core.meta;

import lombok.Data;

/**
 * @description: MRpc Metadata For Service Provider And Consumer To Exchange Information
 * @author: lattice
 * @date: 2022/12/8 4:18 PM
 */
@Data
public class ServiceMetadata {

    /** Service Name **/
    private String serviceAddress;

    /** Service Port **/
    private int servicePort;

    /** Service Name **/
    private String serviceName;

    /** Service Version **/
    private String version;

}

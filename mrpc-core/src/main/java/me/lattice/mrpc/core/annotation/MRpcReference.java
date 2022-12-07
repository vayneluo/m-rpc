package me.lattice.mrpc.core.annotation;

/**
 * @description: MRpc Reference Annotation For Service Consumer To Import Service
 * @author: lattice
 * @date: 2022/12/7 5:45 PM
 */
public @interface MRpcReference {

    /**
     * Service Reference Version, default latest version
     * @return Service Reference Version
     */
    String version() default "latest";

    /**
     * read timeout setting, default 3000ms
     * @return read timeout
     */
    int readTimeout() default 3000;

    /**
     * connection timeout setting, default 3000ms
     * @return connection timeout
     */
    int connectTimeout() default 3000;
}

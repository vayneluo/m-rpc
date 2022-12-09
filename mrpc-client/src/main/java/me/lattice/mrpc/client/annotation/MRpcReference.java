package me.lattice.mrpc.client.annotation;

import java.lang.annotation.*;

/**
 * @description: MRpc Reference Annotation For Service Consumer To Import Service
 * @author: lattice
 * @date: 2022/12/7 5:45 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MRpcReference {

    /**
     * Service Reference Version, default latest version
     * @return Service Reference Version
     */
    String version() default "1.0.0";

    String registryType() default "zookeeper";

    String registryAddress() default "127.0.0.1:2181";

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

package me.lattice.mrpc.webservice.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @description: MRpc Service Annotation For Service Provider To Export Service
 * @author: lattice
 * @date: 2022/12/7 5:35 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Component
public @interface MRpcService {

    /**
     * Service Name Interface
     * @return Service Name
     */
    Class<?> serviceName() default Object.class;

    /**
     * Service Version
     * @return Service Version
     */
    String version() default "1.0";
}

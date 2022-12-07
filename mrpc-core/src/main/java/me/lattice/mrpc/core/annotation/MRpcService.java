package me.lattice.mrpc.core.annotation;

import java.lang.annotation.*;

/**
 * @description: MRpc Service Annotation For Service Provider To Export Service
 * @author: lattice
 * @date: 2022/12/7 5:35 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface MRpcService {

    /**
     * Service Name Interface
     * @return Service Name
     */
    String name() default "";

    /**
     * Service Version
     * @return Service Version
     */
    String version() default "";
}

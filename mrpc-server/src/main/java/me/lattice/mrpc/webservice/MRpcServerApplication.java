package me.lattice.mrpc.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @description: MRpc Server Application Entry Point
 */
@EnableConfigurationProperties
@SpringBootApplication
public class MRpcServerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(MRpcServerApplication.class, args);
    }
}

package com.joker.mmsbackenddepartmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.joker")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.joker.mmsbackendserviceclient.service"})
public class MmsBackendDepartmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmsBackendDepartmentServiceApplication.class, args);
    }

}

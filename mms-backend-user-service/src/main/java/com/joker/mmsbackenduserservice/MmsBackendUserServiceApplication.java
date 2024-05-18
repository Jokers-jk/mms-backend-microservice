package com.joker.mmsbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("com.joker.mmsbackenduserservice.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.joker")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.joker.mmsbackendserviceclient.service"})
public class MmsBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmsBackendUserServiceApplication.class, args);
    }

}

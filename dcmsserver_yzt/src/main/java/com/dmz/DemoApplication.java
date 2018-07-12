package com.dmz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@ComponentScan({"com.dmz"})
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients({"com.dmz.yzt.feign.service"})
@SpringBootApplication
@ServletComponentScan({"com.dmz.yzt.controller"})
public class DemoApplication {

    private static final Logger logger = Logger.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        logger.info("[DCMSServer_yzt]******启动成功！");
    }
}

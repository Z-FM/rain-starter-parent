package org.rain.rain.zuul.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient  //Gateway网关也是eureka的客户端
@SpringBootApplication
public class RainZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainZuulGatewayApplication.class, args);
    }

}

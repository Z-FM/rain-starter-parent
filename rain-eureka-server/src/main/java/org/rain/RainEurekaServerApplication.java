package org.rain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心.
 *
 * @author ZFM.
 * @date 2023/1/7 21:07.
 */
@SpringBootApplication
@EnableEurekaServer
public class RainEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainEurekaServerApplication.class, args);
    }

}

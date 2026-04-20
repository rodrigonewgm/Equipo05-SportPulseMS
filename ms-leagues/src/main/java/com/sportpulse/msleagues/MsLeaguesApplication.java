package com.sportpulse.msleagues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class MsLeaguesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsLeaguesApplication.class, args);
    }
}

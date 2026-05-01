package com.sportpulse.msnotifications.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "http://sportpulse-ms-auth:8081")
public interface AuthClient {
    @PostMapping("/api/auth/validate")
    void validate(@RequestHeader("Authorization") String token);
}

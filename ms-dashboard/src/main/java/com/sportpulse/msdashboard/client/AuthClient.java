package com.sportpulse.msdashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "${auth.service.url}")
public interface AuthClient {

    @PostMapping("/api/auth/validate")
    void validate(@RequestHeader("Authorization") String token);
}
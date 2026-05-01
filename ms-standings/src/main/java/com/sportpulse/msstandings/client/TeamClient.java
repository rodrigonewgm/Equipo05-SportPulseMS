package com.sportpulse.msstandings.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "ms-teams",
        url = "${teams.service.url}"
)
public interface TeamClient {

    @GetMapping("/api/teams/{teamId}")
    Object getTeam(

            @PathVariable Integer teamId

    );


}

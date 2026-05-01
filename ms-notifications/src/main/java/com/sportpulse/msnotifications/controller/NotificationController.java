package com.sportpulse.msnotifications.controller;

import com.sportpulse.msnotifications.client.AuthClient;
import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import com.sportpulse.msnotifications.dto.SubscriptionResponseDTO;
import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthClient authClient;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResponseDTO> subscribe(
            @RequestBody SubscriptionRequestDTO request,
            @RequestHeader("Authorization") String token
    ) {
        authClient.validate(token);

        Subscription sub = notificationService.subscribe(request, token);

        SubscriptionResponseDTO response = new SubscriptionResponseDTO();

        response.setSubscriptionId(sub.getId());
        response.setUserId(sub.getUserId());
        response.setType(sub.getType());
        response.setTeamId(sub.getTeamId());
        response.setEvents(sub.getEvents());
        response.setChannel(sub.getChannel());
        response.setStatus("ACTIVE");
        response.setCreatedAt(sub.getCreatedAt());

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponseDTO>> getSubscriptions(
            @RequestHeader("Authorization") String token
    ) {

        authClient.validate(token);

        List<SubscriptionResponseDTO> result =
                notificationService.getUserSubscriptions(token);

        return ResponseEntity.ok(result);
    }
}

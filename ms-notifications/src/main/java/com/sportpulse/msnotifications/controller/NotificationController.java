package com.sportpulse.msnotifications.controller;

import com.sportpulse.msnotifications.client.AuthClient;
import com.sportpulse.msnotifications.dto.CancelSubscriptionResponseDTO;
import com.sportpulse.msnotifications.dto.SubscriptionListDTO;
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
        try {
            authClient.validate(token);
        } catch (Exception e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "Token inválido o expirado"
            );
        }

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
    public ResponseEntity<List<SubscriptionListDTO>> getSubscriptions(
            @RequestHeader("Authorization") String token
    ) {

        authClient.validate(token);

        List<SubscriptionListDTO> result =
                notificationService.getUserSubscriptions(token);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/subscribe/{id}")
    public ResponseEntity<CancelSubscriptionResponseDTO> cancelSubscription(
            @PathVariable("id") java.util.UUID subscriptionId,
            @RequestHeader("Authorization") String token
    ) {

        authClient.validate(token);

        CancelSubscriptionResponseDTO response =
                notificationService.cancelSubscription(subscriptionId, token);

        return ResponseEntity.ok(response);
    }
}

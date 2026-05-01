package com.sportpulse.msnotifications.service;

import com.sportpulse.msnotifications.dto.CancelSubscriptionResponseDTO;
import com.sportpulse.msnotifications.dto.SubscriptionListDTO;
import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import com.sportpulse.msnotifications.dto.SubscriptionResponseDTO;
import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SubscriptionRepository repository;

    public Subscription subscribe(SubscriptionRequestDTO request, String token) {

        String userId = "user-demo";


        repository.findByUserIdAndTeamIdAndChannelAndActiveTrue(
                userId,
                request.getTeamId(),
                request.getChannel()
        ).ifPresent(s -> {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una suscripción activa"
            );
        });

        Subscription sub = new Subscription();
        sub.setUserId(userId);
        sub.setTeamId(request.getTeamId());
        sub.setType(request.getType());
        sub.setEvents(request.getEvents());
        sub.setChannel(request.getChannel());
        sub.setCreatedAt(java.time.Instant.now());
        sub.setWebhookUrl(request.getWebhookUrl());
        sub.setActive(true);

        return repository.save(sub);
    }

    public List<SubscriptionListDTO> getUserSubscriptions(String token) {

        String userId = "user-demo";

        return repository.findAll().stream()
                .filter(sub -> sub.getUserId().equals(userId) && sub.isActive())
                .map(sub -> {

                    SubscriptionListDTO dto = new SubscriptionListDTO();

                    dto.setSubscriptionId(sub.getId());
                    dto.setType(sub.getType());
                    dto.setTeamId(sub.getTeamId());
                    dto.setEvents(sub.getEvents());
                    dto.setChannel(sub.getChannel());
                    dto.setWebhookUrl(sub.getWebhookUrl());
                    dto.setStatus("ACTIVE");

                    return dto;

                })
                .toList();
    }

    public CancelSubscriptionResponseDTO cancelSubscription(java.util.UUID subscriptionId, String token) {

        String userId = "user-demo";

        Subscription sub = repository.findById(subscriptionId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Suscripción no encontrada"
                ));

        if (!sub.getUserId().equals(userId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "No podés cancelar esta suscripción"
            );
        }

        sub.setActive(false);


        sub.setCancelledAt(Instant.now());

        repository.save(sub);

        CancelSubscriptionResponseDTO response = new CancelSubscriptionResponseDTO();
        response.setSubscriptionId(sub.getId());
        response.setStatus("CANCELLED");
        response.setCancelledAt(sub.getCancelledAt());

        return response;
    }
}

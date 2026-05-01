package com.sportpulse.msnotifications.service;

import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
}

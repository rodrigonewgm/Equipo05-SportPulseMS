package com.sportpulse.msnotifications.service;

import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import com.sportpulse.msnotifications.dto.SubscriptionResponseDTO;
import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<SubscriptionResponseDTO> getUserSubscriptions(String token) {

        // 🔥 por ahora fijo (igual que HU-19)
        String userId = "user-demo";

        return repository.findAll().stream()
                .filter(sub -> sub.getUserId().equals(userId) && sub.isActive())
                .map(sub -> {

                    SubscriptionResponseDTO dto = new SubscriptionResponseDTO();

                    dto.setSubscriptionId(sub.getId());
                    dto.setUserId(sub.getUserId());
                    dto.setType(sub.getType());
                    dto.setTeamId(sub.getTeamId());
                    dto.setEvents(sub.getEvents());
                    dto.setChannel(sub.getChannel());
                    dto.setStatus("ACTIVE");
                    dto.setCreatedAt(sub.getCreatedAt());

                    return dto;

                })
                .toList();
    }
}

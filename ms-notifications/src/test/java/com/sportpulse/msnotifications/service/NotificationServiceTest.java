package com.sportpulse.msnotifications.service;

import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.repository.SubscriptionRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void subscribe_duplicado_lanza409() {

        when(repository.findByUserIdAndTeamIdAndChannelAndActiveTrue(
                anyString(), anyInt(), anyString()
        )).thenReturn(java.util.Optional.of(new Subscription()));

        SubscriptionRequestDTO request = new SubscriptionRequestDTO();
        request.setType("TEAM");
        request.setTeamId(529);
        request.setChannel("LOG");

        assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> notificationService.subscribe(request, "token")
        );
    }

    @Test
    void getUserSubscriptions_sinDatos_devuelveListaVacia() {

        when(repository.findAll()).thenReturn(java.util.List.of());

        var result = notificationService.getUserSubscriptions("token");

        assertTrue(result.isEmpty());
    }
}

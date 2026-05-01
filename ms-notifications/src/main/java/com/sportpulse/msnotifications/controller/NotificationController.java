package com.sportpulse.msnotifications.controller;

import com.sportpulse.msnotifications.client.AuthClient;
import com.sportpulse.msnotifications.dto.CancelSubscriptionResponseDTO;
import com.sportpulse.msnotifications.dto.SubscriptionListDTO;
import com.sportpulse.msnotifications.dto.SubscriptionRequestDTO;
import com.sportpulse.msnotifications.dto.SubscriptionResponseDTO;
import com.sportpulse.msnotifications.entity.Subscription;
import com.sportpulse.msnotifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Suscripciones y alertas de eventos de fútbol")
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthClient authClient;

    @PostMapping("/subscribe")
    @Operation(summary = "Crear suscripción", description = "Suscribe al usuario a alertas de un equipo",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Suscripción creada"),
            @ApiResponse(responseCode = "401", description = "Token inválido o ausente"),
            @ApiResponse(responseCode = "409", description = "Suscripción duplicada")
    })
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
    @Operation(summary = "Listar suscripciones", description = "Devuelve las suscripciones activas del usuario",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponse(responseCode = "200", description = "Lista de suscripciones")
    public ResponseEntity<List<SubscriptionListDTO>> getSubscriptions(
            @RequestHeader("Authorization") String token
    ) {

        authClient.validate(token);

        List<SubscriptionListDTO> result =
                notificationService.getUserSubscriptions(token);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/subscribe/{id}")
    @Operation(summary = "Cancelar suscripción", description = "Cancela una suscripción activa",
            security = @SecurityRequirement(name = "Bearer"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripción cancelada"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
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

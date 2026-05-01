package com.sportpulse.msnotifications.repository;

import com.sportpulse.msnotifications.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndTeamIdAndChannelAndActiveTrue(
            String userId,
            Integer teamId,
            String channel
    );
}

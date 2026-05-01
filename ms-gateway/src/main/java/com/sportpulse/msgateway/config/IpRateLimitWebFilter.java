package com.sportpulse.msgateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportpulse.msgateway.dto.RateLimitExceededResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IpRateLimitWebFilter implements WebFilter {

    private static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private final RateLimitProperties properties;
    private final Clock clock;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, WindowState> windows = new ConcurrentHashMap<>();

    public IpRateLimitWebFilter(RateLimitProperties properties, Clock clock, ObjectMapper objectMapper) {
        this.properties = properties;
        this.clock = clock;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!properties.isEnabled()) {
            return chain.filter(exchange);
        }

        Instant now = Instant.now(clock);
        String clientIp = resolveClientIp(exchange.getRequest());
        WindowState state = windows.compute(clientIp, (key, current) -> updateWindow(current, now));

        if (state.requestCount <= properties.getLimitPerMinute()) {
            return chain.filter(exchange);
        }

        return reject(exchange, state, now);
    }

    private WindowState updateWindow(WindowState current, Instant now) {
        Instant currentMinute = now.truncatedTo(ChronoUnit.MINUTES);

        if (current == null || !current.windowStart.equals(currentMinute)) {
            current = new WindowState(currentMinute, 0);
        }

        current.requestCount++;
        return current;
    }

    private Mono<Void> reject(ServerWebExchange exchange, WindowState state, Instant now) {
        int retryAfterSeconds = calculateRetryAfterSeconds(state.windowStart, now);
        RateLimitExceededResponseDto body = RateLimitExceededResponseDto.tooManyRequests(
                properties.getLimitPerMinute(),
                retryAfterSeconds,
                now.toString()
        );

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().getHeaders().set("Retry-After", String.valueOf(retryAfterSeconds));

        try {
            byte[] payload = objectMapper.writeValueAsBytes(body);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(payload);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(new IllegalStateException("Unable to serialize rate limit response", e));
        }
    }

    private int calculateRetryAfterSeconds(Instant windowStart, Instant now) {
        Instant resetAt = windowStart.plus(1, ChronoUnit.MINUTES);
        long remainingMillis = Duration.between(now, resetAt).toMillis();
        long roundedSeconds = (long) Math.ceil(remainingMillis / 1000.0d);
        return (int) Math.max(1, roundedSeconds);
    }

    private String resolveClientIp(ServerHttpRequest request) {
        String forwardedFor = request.getHeaders().getFirst(FORWARDED_FOR_HEADER);
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }

        return "unknown";
    }

    private static final class WindowState {
        private final Instant windowStart;
        private int requestCount;

        private WindowState(Instant windowStart, int requestCount) {
            this.windowStart = windowStart;
            this.requestCount = requestCount;
        }
    }
}



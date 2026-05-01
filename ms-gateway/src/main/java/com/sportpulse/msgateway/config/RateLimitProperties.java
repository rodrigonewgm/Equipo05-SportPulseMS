package com.sportpulse.msgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sportpulse.gateway.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;

    private int limitPerMinute = 60;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getLimitPerMinute() {
        return limitPerMinute;
    }

    public void setLimitPerMinute(int limitPerMinute) {
        this.limitPerMinute = limitPerMinute;
    }
}



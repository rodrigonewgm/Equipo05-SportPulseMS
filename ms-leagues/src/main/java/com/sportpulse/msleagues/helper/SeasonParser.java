package com.sportpulse.msleagues.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class SeasonParser {

    public Integer getCurrentSeasonYear(JsonNode seasons) {
        if (seasons == null || !seasons.isArray()) return null;
        for (JsonNode s : seasons) {
            if (s.has("current") && s.get("current").asBoolean()) {
                return s.has("year") ? s.get("year").asInt() : null;
            }
        }
        return null;
    }

    public String getCurrentSeasonStart(JsonNode seasons) {
        if (seasons == null || !seasons.isArray()) return null;
        for (JsonNode s : seasons) {
            if (s.has("current") && s.get("current").asBoolean()) {
                return s.has("start") ? s.get("start").asText() : null;
            }
        }
        return null;
    }

    public String getCurrentSeasonEnd(JsonNode seasons) {
        if (seasons == null || !seasons.isArray()) return null;
        for (JsonNode s : seasons) {
            if (s.has("current") && s.get("current").asBoolean()) {
                return s.has("end") ? s.get("end").asText() : null;
            }
        }
        return null;
    }
}

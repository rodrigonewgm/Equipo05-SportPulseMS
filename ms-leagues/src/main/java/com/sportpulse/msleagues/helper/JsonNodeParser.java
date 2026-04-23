package com.sportpulse.msleagues.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class JsonNodeParser {

    public String getText(JsonNode node, String field) {
        return node != null && node.has(field) ? node.get(field).asText() : null;
    }

    public Integer getInt(JsonNode node, String field) {
        return node != null && node.has(field) ? node.get(field).asInt() : null;
    }
}

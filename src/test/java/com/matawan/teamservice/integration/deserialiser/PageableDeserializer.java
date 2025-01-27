package com.matawan.teamservice.integration.deserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public class PageableDeserializer extends JsonDeserializer<Pageable> {

    @Override
    public Pageable deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Safe access to pageNumber and pageSize, defaulting to 0 and 10 respectively
        int pageNumber = node.path("pageNumber").asInt(0);  // Default to 0 if not present
        int pageSize = node.path("pageSize").asInt(10);     // Default to 10 if not present

        return PageRequest.of(pageNumber, pageSize);
    }
}

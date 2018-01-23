package com.Ease.API.RestEasy.Serializer;

import com.Ease.Catalog.Website;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class OnboardingRoomDeserializer extends StdDeserializer<OnboardingRoom> {


    public OnboardingRoomDeserializer() {
        this(null);
    }

    public OnboardingRoomDeserializer(Class<OnboardingRoom> t) {
        super(t);
    }

    @Override
    public OnboardingRoom deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        long id = jsonNode.get("id").asLong(-1);
        String name = jsonNode.get("name").asText();
        OnboardingRoom onboardingRoom = new OnboardingRoom();
        onboardingRoom.setId(id);
        onboardingRoom.setName(name);
        JsonNode arrayNode = jsonNode.get("website_ids");
        if (arrayNode.isArray()) {
            for (JsonNode node : arrayNode) {
                Website website = new Website();
                website.setDb_id(node.intValue());
                onboardingRoom.addWebsite(website);
            }
        }
        return onboardingRoom;
    }
}

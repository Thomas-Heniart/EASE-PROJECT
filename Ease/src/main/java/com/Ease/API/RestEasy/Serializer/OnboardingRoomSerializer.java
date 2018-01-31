package com.Ease.API.RestEasy.Serializer;

import com.Ease.Catalog.Website;
import com.Ease.Team.Onboarding.OnboardingRoom;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class OnboardingRoomSerializer extends StdSerializer<OnboardingRoom> {


    public OnboardingRoomSerializer() {
        this(null);
    }

    public OnboardingRoomSerializer(Class<OnboardingRoom> t) {
        super(t);
    }

    @Override
    public void serialize(OnboardingRoom onboardingRoom, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", onboardingRoom.getId());
        jsonGenerator.writeStringField("name", onboardingRoom.getName());
        jsonGenerator.writeArrayFieldStart("website_ids");
        for (Website website : onboardingRoom.getWebsiteSet())
            jsonGenerator.writeNumber(website.getDb_id());
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}

package com.noredine69.orge.ws.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.threeten.bp.OffsetDateTime;

import java.io.IOException;

public class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {
    public OffsetDateTimeSerializer() {
        this(null);
    }

    public OffsetDateTimeSerializer(final Class<OffsetDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(final OffsetDateTime offsetDateTime, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(offsetDateTime.toString());
    }
}

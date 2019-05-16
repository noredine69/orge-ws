package com.noredine69.orge.ws.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.threeten.bp.LocalDate;

import java.io.IOException;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    public LocalDateSerializer() {
        this(null);
    }

    public LocalDateSerializer(final Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(final LocalDate localDate, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDate.toString());
    }
}

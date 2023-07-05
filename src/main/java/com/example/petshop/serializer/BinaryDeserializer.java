package com.example.petshop.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.types.Binary;
import java.io.IOException;
import java.util.Base64;

public class BinaryDeserializer extends StdDeserializer<Binary> {
    public BinaryDeserializer() {
        this(null);
    }

    public BinaryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Binary deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String base64String = jsonParser.getValueAsString();
        byte[] binaryData = Base64.getDecoder().decode(base64String);
        return new Binary(binaryData);
    }
}

package ir.sam.hearthstone.client.controller.network.serializer_and_deserializer;

import com.google.gson.*;
import ir.sam.hearthstone.client.model.requests.Request;

import java.lang.reflect.Type;

import static ir.sam.hearthstone.client.controller.network.serializer_and_deserializer
        .SerializerAndDeserializerConstants.*;

public class Serializer<T> implements JsonSerializer<T>{

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = src.getClass().getSimpleName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);
        return retValue;
    }
}
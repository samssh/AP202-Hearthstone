package ir.sam.hearthstone.server.controller.network.serializer_and_deserializer;

import com.google.gson.*;

import java.lang.reflect.Type;


import static ir.sam.hearthstone.server.controller.network.serializer_and_deserializer
        .SerializerAndDeserializerConstants.*;

public class Deserializer<T> implements JsonDeserializer<T> {
    private final String packageName;

    public Deserializer(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        Class<?> clazz;
        try {
            clazz = Class.forName(packageName + className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
        return context.deserialize(jsonObject.get(INSTANCE), clazz);
    }
}


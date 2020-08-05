package ir.sam.hearthstone.client.controller.network;

import com.google.gson.*;
import ir.sam.hearthstone.client.model.requests.Request;
import ir.sam.hearthstone.client.model.response.Response;

import java.lang.reflect.Type;

public class CostomSerializerAndDeserialize implements JsonSerializer<Request>, JsonDeserializer<Response> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";
    private static final String RESPONSE_PACKAGE = "ir.sam.hearthstone.client.model.response.";

    @Override
    public JsonElement serialize(Request src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = src.getClass().getSimpleName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);
        return retValue;
    }

    @Override
    public Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        Class<?> clazz;
        try {
            clazz = Class.forName(RESPONSE_PACKAGE + className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
        return context.deserialize(jsonObject.get(INSTANCE), clazz);
    }
}
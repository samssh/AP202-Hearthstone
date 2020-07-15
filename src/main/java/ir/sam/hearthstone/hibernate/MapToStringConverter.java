package ir.sam.hearthstone.hibernate;

import com.google.gson.Gson;
import ir.sam.hearthstone.model.main.ActionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class MapToStringConverter implements AttributeConverter<Map<ActionType, String>, String> {

    @Override
    public String convertToDatabaseColumn(Map<ActionType, String> attribute) {
        return new Gson().toJson(attribute);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<ActionType, String> convertToEntityAttribute(String dbData) {
        return new HashMap<String, String>(new Gson().fromJson(dbData, Map.class)).entrySet()
                .stream().map(entry -> new AbstractMap.SimpleEntry<>(ActionType.valueOf(entry.getKey())
                        , entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

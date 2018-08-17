package ua.home.stat_shop.persistence.converters.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import ua.home.stat_shop.persistence.dto.CategoryCreationDto;
import ua.home.stat_shop.persistence.dto.LocalizedFieldDto;
import ua.home.stat_shop.persistence.dto.FieldDto;
import ua.home.stat_shop.persistence.dto.NonLocalizedFieldDto;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CategoryJsonDeserializer extends JsonDeserializer<CategoryCreationDto> {

    @Value("#{'${available.locales}'.split(',')}")
    Set<String> availableLocales;

    @Override
    public CategoryCreationDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        CategoryCreationDto creationDto = new CategoryCreationDto();
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        if (jsonNode.hasNonNull("categoryId")) {
            String categoryId =  jsonNode.get("categoryId").asText();
            creationDto.setCategoryId(categoryId);
        }
        if (jsonNode.hasNonNull("parent")) {
            String parentId = jsonNode.get("parent").asText();
            creationDto.setParent(parentId);
        }
        boolean translated = jsonNode.get("translated").asBoolean(false);
        FieldDto nameDto;
        if (translated) {
            JsonNode i18n = jsonNode.get("name");
            Map<String, String> translations = availableLocales.stream()
                    .filter(i18n::hasNonNull)
                    .map(locale -> Maps.immutableEntry(locale, i18n.get(locale).asText()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            nameDto = new LocalizedFieldDto(translations);
        } else {
            String name = jsonNode.get("name").asText();
            nameDto = new NonLocalizedFieldDto(name);
        }
        creationDto.setName(nameDto);
        if (jsonNode.hasNonNull("attributes")) {
            Iterator<JsonNode> attributes = jsonNode.get("attributes").elements();
            Set<String> attributesSet = StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(attributes, Spliterator.ORDERED), false)
                    .map(JsonNode::asText).collect(Collectors.toSet());
            creationDto.setAttributes(attributesSet);
        }
        return creationDto;
    }
}

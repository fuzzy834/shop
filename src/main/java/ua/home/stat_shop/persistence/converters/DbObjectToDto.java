package ua.home.stat_shop.persistence.converters;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DbObjectToDto {

    private static List<String> availableLocales;

    public static ProductDto getProductDto(DBObject dbObject) {
        BasicDBObject productBase = (BasicDBObject) dbObject.get("productBase");
        BasicDBObject localizedName = (BasicDBObject) productBase.get("localizedProductName");
        BasicDBObject localizedDescription = (BasicDBObject) productBase.get("localizedProductDescription");
        BasicDBObject category = (BasicDBObject) dbObject.get("category");
        BasicDBObject categoryNameObj = (BasicDBObject) category.get("localizedNames");
        BasicDBList attributes = (BasicDBList) dbObject.get("attributes");

        ProductDto product = new ProductDto(
                dbObject.get("_id").toString(),
                new LocalizedNameDto(getLocalizedFieldValue(localizedName)),
                new LocalizedNameDto(getLocalizedFieldValue(localizedDescription)),
                productBase.getDouble("retailPrice"),
                productBase.getDouble("bulkPrice"),
                productBase.getString("currency"),
                new LocalizedNameDto(getLocalizedFieldValue(categoryNameObj)),
                getAttributesFromProduct(attributes)
        );

        if (dbObject.containsField("discount")) {
            BasicDBObject discount = (BasicDBObject) dbObject.get("discount");
            product.setDiscount(discount.getInt("percentOff"));
        }

        return product;
    }

    private static List<ProductAttributeDto> getAttributesFromProduct(BasicDBList attributes) {
        return attributes.stream().map(attribute -> {
            String id = ((BasicDBObject) attribute).getString("attributeId");
            Object nameObj = ((BasicDBObject) attribute).get("name");
            BasicDBObject nameDbObj = (BasicDBObject) nameObj;
            NameDto name;
            if (nameDbObj.containsField("nonLocalizedName")) {
                String nameStr = nameDbObj.getString("nonLocalizedName");
                name = new NonLocalizedNameDto(nameStr);
            } else {
                BasicDBObject localizedName = (BasicDBObject) nameDbObj.get("localizedName");
                name = new LocalizedNameDto(getLocalizedFieldValue(localizedName));
            }
            Integer priority = ((BasicDBObject) attribute).getInt("priority", 0);
            Object valueObj = ((BasicDBObject) attribute).get("values");
            BasicDBList valuesDbList = (BasicDBList) valueObj;
            Set<AttributeValueDto> values = valuesDbList.stream().map(v -> {
                BasicDBObject valueDbObj = (BasicDBObject) v;
                String valueId = valueDbObj.getString("_id");
                String quantity = valueDbObj.containsField("quantity") ? valueDbObj.getString("quantity") + " " : "";
                AttributeValueDto value;
                if (valueDbObj.containsField("nonLocalizedValue")) {
                    String nonLocalizedValue = quantity + valueDbObj.getString("nonLocalizedValue");
                    value = new AttributeNonLocalizedValueDto(valueId, quantity, nonLocalizedValue);
                } else {
                    BasicDBObject localizedValues = (BasicDBObject) valueDbObj.get("localizedValue");
                    value = new AttributeLocalizedValueDto(valueId, quantity, getLocalizedFieldValue(localizedValues));
                }
                return value;
            }).collect(Collectors.toSet());
            return new ProductAttributeDto(id, priority, name, values);
        }).sorted((p1, p2) -> p2.getPriority().compareTo(p1.getPriority())).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static CategoryDto getCategoryDto(DBObject dbObject) {
        String subCategory = dbObject.get("subCategory").toString();
        CategoryDto category = new CategoryDto();
        category.setId(dbObject.get("_id").toString());
        BasicDBObject localizedNames = (BasicDBObject) dbObject.get("localizedNames");
        category.setName(new LocalizedNameDto(getLocalizedFieldValue(localizedNames)));
        category.setChildren(Sets.newHashSet());
        BasicDBObject attributes = (BasicDBObject) dbObject.get("attributes");
        category.setAttributes(attributes.toMap());
        if (Boolean.valueOf(subCategory)) {
            category.setParent(((BasicDBObject) dbObject.get("parent")).getString("_id"));
        }
        return category;
    }

    public static AttributeDto getAttributeDto(DBObject dbObject) {
        AttributeDto attribute = new AttributeDto();
        attribute.setAttributeId(dbObject.get("_id").toString());

        BasicDBObject attributeName = (BasicDBObject) dbObject.get("attributeName");

        if (attributeName.containsField("nonLocalizedName")) {
            attribute.setName(new NonLocalizedNameDto(attributeName.getString("nonLocalizedName")));
        } else {
            BasicDBObject localizedNames = (BasicDBObject) attributeName.get("localizedName");
            attribute.setName(new LocalizedNameDto(getLocalizedFieldValue(localizedNames)));
        }

        attribute.setPriority(attributeName.getInt("priority", 0));

        BasicDBList attributeValues = (BasicDBList) dbObject.get("attributeValues");

        Set<AttributeValueDto> values = attributeValues.stream().map(value ->
                {
                    BasicDBObject valueObj = (BasicDBObject) value;
                    String quantity = valueObj.containsField("quantity") ? valueObj.getString("quantity") : "";
                    String id = valueObj.getString("_id");
                    if (valueObj.containsField("nonLocalizedValue")) {
                        return new AttributeNonLocalizedValueDto(id, quantity, valueObj.getString("nonLocalizedValue"));
                    } else {
                        BasicDBObject localizedValues = (BasicDBObject) valueObj.get("localizedValue");
                        return new AttributeLocalizedValueDto(id, quantity, getLocalizedFieldValue(localizedValues));
                    }
                }
        ).collect(Collectors.toSet());

        attribute.setValues(values);

        return attribute;
    }

    private static Map<String, String> getLocalizedFieldValue(BasicDBObject localizedField) {
        return availableLocales.stream()
                .filter(localizedField::containsField)
                .map(locale -> Maps.immutableEntry(locale, localizedField.getString(locale)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Value("#{'${available.locales}'.split(',')}")
    public void setAvailableLocales(List<String> availableLocales) {
        DbObjectToDto.availableLocales = availableLocales;
    }
}

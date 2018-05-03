package ua.home.stat_shop.persistence.converters;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.ProductAttributeDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DbObjectToDto {

    public static ProductDto getProductDto(DBObject dbObject, String lang) {
        BasicDBObject productBase = (BasicDBObject) dbObject.get("productBase");
        BasicDBObject localizedName = (BasicDBObject) productBase.get("localizedProductName");
        BasicDBObject localizedDescription = (BasicDBObject) productBase.get("localizedProductDescription");
        BasicDBObject category = (BasicDBObject) dbObject.get("category");
        BasicDBList attributes = (BasicDBList) dbObject.get("attributes");

        ProductDto product = new ProductDto(
                dbObject.get("_id").toString(),
                localizedName.getString(lang),
                localizedDescription.getString(lang),
                productBase.getDouble("retailPrice"),
                productBase.getDouble("bulkPrice"),
                productBase.getString("currency"),
                getCategoryNameFromProduct(category, lang),
                getAttributesFromProduct(attributes, lang)
        );

        if (dbObject.containsField("discount")) {
            BasicDBObject discount = (BasicDBObject) dbObject.get("discount");
            product.setDiscount(discount.getInt("percentOff"));
        }

        return product;
    }

    private static String getCategoryNameFromProduct(BasicDBObject category, String lang) {
        BasicDBObject nameObj = (BasicDBObject) category.get("localizedNames");
        return nameObj.getString(lang);
    }

    private static List<ProductAttributeDto> getAttributesFromProduct(BasicDBList attributes, String lang) {
        return attributes.stream().map(attribute -> {
            String id = ((BasicDBObject) attribute).getString("attributeId");
            Object nameObj = ((BasicDBObject) attribute).get("name");
            BasicDBObject nameDbObj = (BasicDBObject) nameObj;
            String name;
            if (nameDbObj.containsField("nonLocalizedName")) {
                name = nameDbObj.getString("nonLocalizedName");
            } else {
                BasicDBObject localizedName = (BasicDBObject) nameDbObj.get("localizedName");
                name = localizedName.getString(lang);
            }
            Integer priority = ((BasicDBObject) attribute).getInt("priority", 0);
            Object valueObj = ((BasicDBObject) attribute).get("value");
            BasicDBObject valueDbObj = (BasicDBObject) valueObj;
            String quantity = valueDbObj.containsField("quantity") ? valueDbObj.getString("quantity") + " " : "";
            String value;
            if (valueDbObj.containsField("nonLocalizedValue")) {
                value = quantity + valueDbObj.getString("nonLocalizedValue");
            } else {
                BasicDBObject localizedValue = (BasicDBObject) valueDbObj.get("localizedValue");
                value = quantity + localizedValue.getString(lang);
            }
            return new ProductAttributeDto(id, priority, name, value);
        }).sorted((p1, p2) -> p2.getPriority().compareTo(p1.getPriority())).collect(Collectors.toList());
    }

    public static CategoryDto getCategoryDto(DBObject dbObject, String lang) {
        String subCategory = dbObject.get("subCategory").toString();
        CategoryDto category = new CategoryDto();
        category.setId(dbObject.get("_id").toString());
        category.setName(((BasicDBObject) dbObject.get("localizedNames")).getString(lang));
        category.setChildren(Sets.newHashSet());
        if (Boolean.valueOf(subCategory)) {
            category.setParent(((BasicDBObject) dbObject.get("parent")).getString("$id"));
        }
        return category;
    }

    public static AttributeDto getAttributeDto(DBObject dbObject, String lang) {
        AttributeDto attribute = new AttributeDto();
        attribute.setAttributeId(dbObject.get("_id").toString());

        BasicDBObject attributeName = (BasicDBObject) dbObject.get("attributeName");

        if (attributeName.containsField("nonLocalizedName")) {
            attribute.setName(attributeName.getString("nonLocalizedName"));
        } else {
            BasicDBObject localizedNames = (BasicDBObject) attributeName.get("localizedName");
            attribute.setName(localizedNames.getString(lang));
        }

        attribute.setPriority(attributeName.getInt("priority", 0));

        BasicDBList attributeValues = (BasicDBList) dbObject.get("attributeValues");

        Map<String, String> values = attributeValues.stream().map(value ->
                {
                    BasicDBObject valueObj = (BasicDBObject) value;
                    String quantity = valueObj.containsField("quantity") ? valueObj.getString("quantity") + " " : "";
                    String id = valueObj.getString("_id");
                    if (valueObj.containsField("nonLocalizedValue")) {
                        return Maps.immutableEntry(id, quantity + valueObj.getString("nonLocalizedValue"));
                    } else {
                        BasicDBObject localizedValues = (BasicDBObject) valueObj.get("localizedValue");
                        return Maps.immutableEntry(id, quantity + localizedValues.getString(lang));
                    }
                }
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        attribute.setValues(values);

        return attribute;
    }
}

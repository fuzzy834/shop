package ua.home.stat_shop.persistence.converters;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;
import java.util.stream.Collectors;

public class DbObjectToDto {

    public static ProductDto getProductDto(DBObject dbObject, String lang) {
        return new ProductDto(
                dbObject.get("_id").toString(),
                Double.parseDouble(dbObject.get("retailPrice").toString()),
                Double.parseDouble(dbObject.get("bulkPrice").toString()),
                Integer.parseInt(dbObject.containsField("discount") ? dbObject.get("discount").toString() : "0"),
                getCategoryNameFromProduct((BasicDBObject) dbObject.get("category"), lang),
                getAttributesFromProduct((BasicDBList) dbObject.get("attributes"), lang)
        );
    }

    private static String getCategoryNameFromProduct(BasicDBObject category, String lang) {
        BasicDBObject nameObj = (BasicDBObject) category.get("localizedNames");
        return nameObj.getString(lang);
    }

    private static Map<String, String> getAttributesFromProduct(BasicDBList attributes, String lang) {
        return attributes.stream().map(attribute -> {

            Object nameObj = ((BasicDBObject) attribute).get("name");
            BasicDBObject nameDbObj = (BasicDBObject) nameObj;
            String name;
            if (nameDbObj.containsField("nonLocalizedName")) {
                name = nameDbObj.getString("nonLocalizedName");
            } else {
                BasicDBObject localizedName = (BasicDBObject) nameDbObj.get("localizedName");
                name = localizedName.getString(lang);
            }

            Object valueObj = ((BasicDBObject) attribute).get("value");
            BasicDBObject valueDbObj = (BasicDBObject) valueObj;
            String value;
            if (valueDbObj.containsField("nonLocalizedValue")) {
                value = valueDbObj.getString("nonLocalizedValue");
            } else {
                BasicDBObject localizedValue = (BasicDBObject) valueDbObj.get("localizedValue");
                value = localizedValue.getString(lang);
            }

            return Maps.immutableEntry(name, value);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
}

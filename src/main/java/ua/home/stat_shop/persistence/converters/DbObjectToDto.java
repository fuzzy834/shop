package ua.home.stat_shop.persistence.converters;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.domain.Image;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.AttributeValueDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.LocalizedFieldDto;
import ua.home.stat_shop.persistence.dto.FieldDto;
import ua.home.stat_shop.persistence.dto.NonLocalizedFieldDto;
import ua.home.stat_shop.persistence.dto.ProductAttributeDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DbObjectToDto {

    private static List<String> availableLocales;

    public static ProductDto getProductDto(DBObject dbObject) {
        BasicDBObject productBase = (BasicDBObject) dbObject.get("productBase");
        BasicDBObject localizedName = (BasicDBObject) productBase.get("productName");
        BasicDBObject localizedDescription = (BasicDBObject) productBase.get("productDescription");
        BasicDBObject category = (BasicDBObject) dbObject.get("category");
        BasicDBObject categoryNameObj = (BasicDBObject) category.get("categoryName");
        BasicDBList attributes = (BasicDBList) dbObject.get("attributes");

        ProductDto product = new ProductDto(
                dbObject.get("_id").toString(),
                new LocalizedFieldDto(getLocalizedFieldValue(localizedName)),
                new LocalizedFieldDto(getLocalizedFieldValue(localizedDescription)),
                productBase.getDouble("retailPrice"),
                productBase.getDouble("bulkPrice"),
                productBase.getString("currency"),
                new LocalizedFieldDto(getLocalizedFieldValue(categoryNameObj)),
                getAttributesFromProduct(attributes)
        );

        if (dbObject.containsField("discount")) {
            BasicDBObject discount = (BasicDBObject) dbObject.get("discount");
            product.setDiscount(discount.getInt("percentOff"));
        }

        if (dbObject.containsField("images")) {
            BasicDBList images = (BasicDBList) dbObject.get("images");
            Set<Image> imagePaths =  images.stream()
                    .map(image -> {
                               BasicDBObject imageDbObj = (BasicDBObject) image;
                               Image img = new Image();
                               img.setPath(imageDbObj.getString("path"));
                               img.setSize(imageDbObj.getLong("size"));
                               img.setType(imageDbObj.getString("type"));
                               return img;
                            }
                    )
                    .collect(Collectors.toSet());
            product.setImages(imagePaths);
        }
        if (dbObject.containsField("videoUrl")) {
            product.setVideoUrl(dbObject.get("videoUrl").toString());
        }
        return product;
    }

    private static List<ProductAttributeDto> getAttributesFromProduct(BasicDBList attributes) {
        return attributes.stream().map(attribute -> {
            String id = ((BasicDBObject) attribute).getString("attributeId");
            Object nameObj = ((BasicDBObject) attribute).get("name");
            BasicDBObject nameDbObj = (BasicDBObject) nameObj;
            FieldDto name = getNameDto(nameDbObj);
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
                    value = new AttributeValueDto(valueId, quantity, new NonLocalizedFieldDto(nonLocalizedValue));
                } else {
                    BasicDBObject localizedValues = (BasicDBObject) valueDbObj.get("localizedValue");
                    value = new AttributeValueDto(valueId, quantity,
                            new LocalizedFieldDto(getLocalizedFieldValue(localizedValues)));
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
        BasicDBObject categoryName = (BasicDBObject) dbObject.get("categoryName");
        FieldDto name;
        name = getNameDto(categoryName);
        category.setName(name);
        category.setChildren(Sets.newHashSet());
        BasicDBList attributes = (BasicDBList) dbObject.get("attributes");
        category.setAttributes(attributes.stream().filter(Objects::nonNull).map(String.class::cast).collect(Collectors.toSet()));
        DBRef parent = (DBRef) dbObject.get("parent");
        if (Objects.nonNull(parent)) {
            category.setParent(parent.getId().toString());
        }
        return category;
    }

    private static FieldDto getNameDto(BasicDBObject nameObj) {
        FieldDto name;
        if (nameObj.containsField("nonLocalizedField")) {
            String nameStr = nameObj.getString("nonLocalizedField");
            name = new NonLocalizedFieldDto(nameStr);
        } else {
            BasicDBObject localizedName = (BasicDBObject) nameObj.get("localizedField");
            name = new LocalizedFieldDto(getLocalizedFieldValue(localizedName));
        }
        return name;
    }

    public static AttributeDto getAttributeDto(DBObject dbObject) {
        AttributeDto attribute = new AttributeDto();
        attribute.setAttributeId(dbObject.get("_id").toString());

        BasicDBObject attributeName = (BasicDBObject) dbObject.get("attributeName");

        if (attributeName.containsField("nonLocalizedField")) {
            attribute.setName(new NonLocalizedFieldDto(attributeName.getString("nonLocalizedField")));
        } else {
            BasicDBObject localizedNames = (BasicDBObject) attributeName.get("localizedField");
            attribute.setName(new LocalizedFieldDto(getLocalizedFieldValue(localizedNames)));
        }

        attribute.setPriority(attributeName.getInt("priority", 0));

        BasicDBList attributeValues = (BasicDBList) dbObject.get("attributeValues");

        Set<AttributeValueDto> values = attributeValues.stream().map(value ->
                {
                    BasicDBObject valueObj = (BasicDBObject) value;
                    String quantity = valueObj.containsField("quantity") ? valueObj.getString("quantity") : "";
                    String id = valueObj.getString("_id");
                    if (valueObj.containsField("nonLocalizedValue")) {
                        return new AttributeValueDto(
                                id, quantity,
                                new NonLocalizedFieldDto(valueObj.getString("nonLocalizedValue")));
                    } else {
                        BasicDBObject localizedValues = (BasicDBObject) valueObj.get("localizedValue");
                        return new AttributeValueDto(id, quantity,
                                new LocalizedFieldDto(getLocalizedFieldValue(localizedValues)));
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

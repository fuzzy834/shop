package ua.home.stat_shop.persistence.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DTOType(dtoTypes = ProductDto.class)
public class ProductAttribute {

    @DTOField(dtoTypes = ProductDto.class)
    private String attributeId;
    private Boolean temporal;
    @DTOField(dtoTypes = ProductDto.class)
    private Integer priority;
    @DTOField(dtoTypes = ProductDto.class)
    private AttributeName name;
    @DTOField(dtoTypes = ProductDto.class)
    private Set<AttributeValue> values;
    private Set<String> valueIds;

    public ProductAttribute(Attribute attribute, Set<AttributeValue> values) {
        this.attributeId = attribute.getId();
        this.temporal = false;
        this.priority = attribute.getAttributeName().getPriority();
        this.name = attribute.getAttributeName();
        this.values = values;
        this.valueIds = this.values.stream().map(AttributeValue::getId).collect(Collectors.toSet());
    }
}

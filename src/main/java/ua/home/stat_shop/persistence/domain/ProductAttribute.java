package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    private String attributeId;
    private AttributeName name;
    private AttributeValue value;

    public ProductAttribute(Attribute attribute, AttributeValue value) {
        this.attributeId = attribute.getId();
        this.name = attribute.getAttributeName();
        this.value = value;
    }
}

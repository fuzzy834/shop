package ua.home.stat_shop.persistence.domain;

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
    private Boolean temporal;
    private Integer priority;
    private AttributeName name;
    private AttributeValue value;

    public ProductAttribute(Attribute attribute, AttributeValue value) {
        this.attributeId = attribute.getId();
        this.temporal = false;
        this.priority = attribute.getAttributeName().getPriority();
        this.name = attribute.getAttributeName();
        this.value = value;
    }
}

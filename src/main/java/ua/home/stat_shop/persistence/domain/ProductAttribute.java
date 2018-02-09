package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {

    private String attributeId;
    private Boolean temporal;
    private Integer priority;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AttributeName name;
    private AttributeValue value;

    public ProductAttribute(Attribute attribute, AttributeValue value) {
        this.attributeId = attribute.getId();
        this.temporal = false;
        this.priority = attribute.getAttributeName().getPriority();
        this.name = attribute.getAttributeName();
        this.value = value;
    }

    public ProductAttribute(LocalDateTime startDateTime, LocalDateTime endDateTime, Attribute attribute, AttributeValue value) {
        this.attributeId = attribute.getId();
        this.temporal = true;
        this.priority = attribute.getAttributeName().getPriority();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.name = attribute.getAttributeName();
        this.value = value;
    }
}

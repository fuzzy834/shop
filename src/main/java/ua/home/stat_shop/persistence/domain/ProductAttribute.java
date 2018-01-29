package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import ua.home.stat_shop.persistence.constants.AttributeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

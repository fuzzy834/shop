package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Attribute {

    @Id
    private String id;
    private AttributeName attributeName;
    private Set<AttributeValue> attributeValues;

    public Attribute(AttributeName attributeName, Set<AttributeValue> attributeValues) {
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
    }
}



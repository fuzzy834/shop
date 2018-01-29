package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.home.stat_shop.persistence.constants.AttributeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attribute {

    @Id
    private String id;
    private Boolean filterOn;
    private AttributeName attributeName;
    private Set<AttributeValue> attributeValues;

    public Attribute(AttributeName attributeName, Set<AttributeValue> attributeValues, Boolean filterOn) {
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
        this.filterOn = filterOn;
    }
}



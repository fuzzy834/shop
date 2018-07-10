package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
@DTOType(dtoTypes = AttributeDto.class, base = true)
public class Attribute {

    @Id
    @DTOField(dtoTypes = AttributeDto.class)
    private String id;
    @DTOField(dtoTypes = AttributeDto.class)
    private AttributeName attributeName;
    @DTOField(dtoTypes = AttributeDto.class)
    private Set<AttributeValue> attributeValues;

    public Attribute(AttributeName attributeName, Set<AttributeValue> attributeValues) {
        this.attributeName = attributeName;
        this.attributeValues = attributeValues;
    }
}



package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductAttributeDto {

    private String attributeId;

    private Integer priority;

    private NameDto name;

    private Set<AttributeValueDto> values;
}

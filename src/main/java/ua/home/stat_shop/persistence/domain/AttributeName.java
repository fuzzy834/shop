package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

@Getter
@Setter
@DTOType(dtoTypes = {AttributeDto.class, ProductDto.class})
public class AttributeName {

    @DTOField(dtoTypes = {AttributeDto.class, ProductDto.class})
    private Integer priority;
}

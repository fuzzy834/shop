package ua.home.stat_shop.persistence.domain;

import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

@DTOType(dtoTypes = {ProductDto.class, CategoryDto.class, AttributeDto.class})
public class Field {
}

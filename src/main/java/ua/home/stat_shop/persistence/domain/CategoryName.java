package ua.home.stat_shop.persistence.domain;

import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

@DTOType(dtoTypes = {CategoryDto.class, ProductDto.class})
public class CategoryName {
}

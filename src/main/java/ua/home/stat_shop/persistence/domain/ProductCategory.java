package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DTOType(dtoTypes = ProductDto.class)
public class ProductCategory {

    @DTOField(dtoTypes = ProductDto.class)
    private String categoryId;

    @DTOField(dtoTypes = ProductDto.class)
    private Field categoryName;

    public ProductCategory(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
    }
}

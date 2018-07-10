package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DTOType(dtoTypes = ProductDto.class)
public class ProductCategory {

    @DTOField(dtoTypes = ProductDto.class)
    private String categoryId;

    private Set<String> ancestors;

    @DTOField(dtoTypes = ProductDto.class, i18n = true)
    private Map<String, String> localizedNames;

    public ProductCategory(Category category) {
        this.categoryId = category.getId();
        this.ancestors = category.getAncestors();
        this.localizedNames = category.getLocalizedNames();
    }
}

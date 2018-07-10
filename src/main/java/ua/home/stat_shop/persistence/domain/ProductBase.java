package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DTOType(dtoTypes = ProductDto.class)
public class ProductBase {

    @DTOField(dtoTypes = ProductDto.class, i18n = true)
    private Map<String, String> localizedProductName;
    @DTOField(dtoTypes = ProductDto.class, i18n = true)
    private Map<String, String> localizedProductDescription;
    @DTOField(dtoTypes = ProductDto.class)
    private Double retailPrice;
    @DTOField(dtoTypes = ProductDto.class)
    private Double bulkPrice;
    @DTOField(dtoTypes = ProductDto.class)
    private String currency;
}

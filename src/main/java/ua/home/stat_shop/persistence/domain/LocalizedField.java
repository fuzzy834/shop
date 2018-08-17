package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DTOType(dtoTypes = {ProductDto.class, CategoryDto.class, AttributeDto.class})
public class LocalizedField extends Field {

    @DTOField(dtoTypes = {ProductDto.class, CategoryDto.class, AttributeDto.class}, i18n = true)
    private Map<String, String> localizedField;
}

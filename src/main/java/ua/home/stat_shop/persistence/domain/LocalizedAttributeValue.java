package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DTOType(dtoTypes = {AttributeDto.class, ProductDto.class})
public class LocalizedAttributeValue extends AttributeValue {

    @DTOField(dtoTypes = {AttributeDto.class, ProductDto.class}, i18n = true)
    private Map<String, String> localizedValue;
}

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
public class Image {

    @DTOField(dtoTypes = ProductDto.class)
    private String path;

    @DTOField(dtoTypes = ProductDto.class)
    private Long size;

    @DTOField(dtoTypes = ProductDto.class)
    private String type;
}

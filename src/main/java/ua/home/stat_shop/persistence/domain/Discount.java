package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DTOType(dtoTypes = ProductDto.class)
public class Discount {

    @DTOField(dtoTypes = ProductDto.class)
    Integer percentOff;
    LocalDateTime start;
    LocalDateTime end;
}

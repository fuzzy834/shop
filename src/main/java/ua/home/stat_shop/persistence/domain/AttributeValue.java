package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.UUID;

@Getter
@Setter
@DTOType(dtoTypes = {AttributeDto.class, ProductDto.class})
public class AttributeValue {

    @Id
    @DTOField(dtoTypes = {AttributeDto.class, ProductDto.class})
    private String id;

    private Integer quantity;

    private String categoryId;

    public AttributeValue() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
}

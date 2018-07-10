package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto implements Serializable {

    private String id;

    private NameDto name;

    private NameDto description;

    private Double retailPrice;

    private Double bulkPrice;

    private Integer discount;

    private String currency;

    private NameDto category;

    private List<ProductAttributeDto> attributes;

    public ProductDto(String id, NameDto name, NameDto description, Double retailPrice,
                      Double bulkPrice, String currency, NameDto category,
                      List<ProductAttributeDto> attributes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.retailPrice = retailPrice;
        this.bulkPrice = bulkPrice;
        this.currency = currency;
        this.category = category;
        this.attributes = attributes;
    }
}

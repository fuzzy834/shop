package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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

    private String name;

    private String description;

    private Double retailPrice;

    private Double bulkPrice;

    private Integer discount;

    private String currency;

    private String category;

    private List<ProductAttributeDto> attributes;

    public ProductDto(String id, String name, String description, Double retailPrice,
                      Double bulkPrice, String currency, String category,
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

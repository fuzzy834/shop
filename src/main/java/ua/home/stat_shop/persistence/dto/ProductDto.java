package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.home.stat_shop.persistence.domain.Image;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto implements Serializable {

    private String id;

    private FieldDto name;

    private FieldDto description;

    private Double retailPrice;

    private Double bulkPrice;

    private Integer discount;

    private String currency;

    private FieldDto category;

    private List<ProductAttributeDto> attributes;

    private Set<Image> images;

    private String videoUrl;

    public ProductDto(String id, FieldDto name, FieldDto description, Double retailPrice,
                      Double bulkPrice, String currency, FieldDto category,
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

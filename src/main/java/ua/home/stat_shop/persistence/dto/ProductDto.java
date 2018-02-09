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

    private String currency;

    private String category;

    private List<ProductAttributeDto> attributes;
}

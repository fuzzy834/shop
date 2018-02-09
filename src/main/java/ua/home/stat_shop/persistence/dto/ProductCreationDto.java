package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ua.home.stat_shop.persistence.domain.ProductBase;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductCreationDto implements Serializable {

    private String productId;

    private ProductBase productBase;

    private String category;

    private Map<String, String> attributeValueMap;
}

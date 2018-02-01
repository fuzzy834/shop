package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductDto implements Serializable {

    private String id;

    private Double retailPrice;

    private Double bulkPrice;

    private Integer discount;

    private String category;

    private Map<String, String> attributes;
}

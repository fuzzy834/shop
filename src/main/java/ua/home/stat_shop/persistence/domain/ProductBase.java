package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBase {

    private Map<String, String> localizedProductName;
    private Map<String, String> localizedProductDescription;
    private Double retailPrice;
    private Double bulkPrice;
    private String currency;
}

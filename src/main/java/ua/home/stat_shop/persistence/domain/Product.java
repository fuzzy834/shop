package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Product {

    @Id
    private String id;
    private Double retailPrice;
    private Double bulkPrice;
    private Integer discount;
    private ProductCategory category;
    private Set<ProductAttribute> attributes;

    public Product(Double retailPrice, Double bulkPrice, ProductCategory category, Set<ProductAttribute> attributes) {
        this.retailPrice = retailPrice;
        this.bulkPrice = bulkPrice;
        this.category = category;
        this.attributes = attributes;
    }

    public Product(Double retailPrice, Double bulkPrice, Integer discount, ProductCategory category, Set<ProductAttribute> attributes) {
        this.retailPrice = retailPrice;
        this.bulkPrice = bulkPrice;
        this.discount = discount;
        this.category = category;
        this.attributes = attributes;
    }
}

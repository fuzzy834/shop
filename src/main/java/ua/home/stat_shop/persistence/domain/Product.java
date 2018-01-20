package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;
    private Double retailPrice;
    private Double bulkPrice;
    private Integer discount;

    @DBRef
    private Category category;
    private Set<Attribute> attributes;

    public Product(Double retailPrice, Double bulkPrice, Category category, Set<Attribute> attributes) {
        this.retailPrice = retailPrice;
        this.bulkPrice = bulkPrice;
        this.category = category;
        this.attributes = attributes;
    }

    public Product(Double retailPrice, Double bulkPrice, Integer discount, Category category, Set<Attribute> attributes) {
        this.retailPrice = retailPrice;
        this.bulkPrice = bulkPrice;
        this.discount = discount;
        this.category = category;
        this.attributes = attributes;
    }
}

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
    private ProductCategory category;
    private Set<ProductAttribute> attributes;
    private ProductBase productBase;
    private Discount discount;

    public Product(ProductBase productBase, ProductCategory category, Set<ProductAttribute> attributes) {
        this.productBase = productBase;
        this.category = category;
        this.attributes = attributes;
    }
}

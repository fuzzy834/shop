package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@DTOType(dtoTypes = ProductDto.class, base = true)
public class Product {

    @Id
    @DTOField(dtoTypes = ProductDto.class)
    private String id;
    @DTOField(dtoTypes = ProductDto.class)
    private ProductCategory category;
    @DTOField(dtoTypes = ProductDto.class)
    private Set<ProductAttribute> attributes;
    @DTOField(dtoTypes = ProductDto.class)
    private ProductBase productBase;
    @DTOField(dtoTypes = ProductDto.class)
    private Discount discount;

    public Product(ProductBase productBase, ProductCategory category, Set<ProductAttribute> attributes) {
        this.productBase = productBase;
        this.category = category;
        this.attributes = attributes;
    }
}

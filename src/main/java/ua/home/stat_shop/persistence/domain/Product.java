package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    @Id
    private String id;
    private Double retailPrice;
    private Double bulkPrice;
    private Integer discount;

    @JsonIgnore
    @DBRef(lazy = true)
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

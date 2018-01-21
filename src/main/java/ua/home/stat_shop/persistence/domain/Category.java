package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Category {

    @Id
    private String id;
    private Boolean subCategory;
    @DBRef
    private Category category;
    private Map<String, String> localizedNames;

    public Category(Map<String, String> localizedNames) {
        this.subCategory = false;
        this.localizedNames = localizedNames;
    }

    public Category(Map<String, String> localizedNames, Category category) {
        this.subCategory = true;
        this.category = category;
    }
}

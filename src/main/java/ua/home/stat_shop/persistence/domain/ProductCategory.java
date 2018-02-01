package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

    private String categoryId;

    private Set<String> ancestors;

    private Map<String, String> localizedNames;

    public ProductCategory(Category category) {
        this.categoryId = category.getId();
        this.ancestors = category.getAncestors();
        this.localizedNames = category.getLocalizedNames();
    }
}

package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

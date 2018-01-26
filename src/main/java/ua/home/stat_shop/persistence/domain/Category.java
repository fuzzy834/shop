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

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    @Id
    private String id;

    private Boolean subCategory;

    @DBRef(lazy = true)
    @JsonIgnore
    private Category parent;

    private Set<String> ancestors;

    private Map<String, String> localizedNames;

    public Category(Map<String, String> localizedNames) {
        this.subCategory = false;
        this.localizedNames = localizedNames;
    }

    public Category(Map<String, String> localizedNames, Category parent) {
        this.subCategory = true;
        this.parent = parent;
        this.localizedNames = localizedNames;
        if (null == this.ancestors) {
            ancestors = new HashSet<>();
        }
        this.ancestors.add(parent.getId());
        if (!Objects.isNull(parent.getAncestors()) && !parent.getAncestors().isEmpty()) {
            this.ancestors.addAll(parent.getAncestors());
        }
    }
}

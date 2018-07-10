package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;
import ua.home.stat_shop.persistence.dto.CategoryDto;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@DTOType(dtoTypes = {CategoryDto.class}, base = true)
public class Category {

    @Id
    @DTOField(dtoTypes = {CategoryDto.class})
    private String id;

    @DTOField(dtoTypes = {CategoryDto.class})
    private Boolean subCategory;

    @DTOField(dtoTypes = {CategoryDto.class}, refToSelf = "id")
    private Category parent;

    private Set<String> ancestors;

    @DTOField(dtoTypes = {CategoryDto.class}, i18n = true)
    private Map<String, String> localizedNames;

    @DTOField(dtoTypes = {CategoryDto.class})
    private Map<String, Integer> attributes = new HashMap<>();

    public Category(Map<String, String> localizedNames) {
        this.subCategory = false;
        this.localizedNames = localizedNames;
    }

    public Category(Map<String, String> localizedNames, Category parent) {
        this.subCategory = true;
        this.parent = parent;
        this.localizedNames = localizedNames;
        if (Objects.isNull(this.ancestors)) {
            ancestors = new HashSet<>();
        }
        this.ancestors.add(parent.getId());
        if (!Objects.isNull(parent.getAncestors()) && !parent.getAncestors().isEmpty()) {
            this.ancestors.addAll(parent.getAncestors());
        }
    }
}

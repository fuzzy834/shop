package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    @DTOField(dtoTypes = {CategoryDto.class}, refToSelf = "id")
    private Category parent;

    @DTOField(dtoTypes = {CategoryDto.class})
    private Field categoryName;

    @DTOField(dtoTypes = {CategoryDto.class})
    private Set<String> attributes = new HashSet<>();

    public Category(Field name) {
        this.subCategory = false;
        this.categoryName = name;
    }

    public Category(Field name, Category parent) {
        this.subCategory = true;
        this.parent = parent;
        this.categoryName = name;
    }
}

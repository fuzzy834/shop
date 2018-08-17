package ua.home.stat_shop.persistence.repository;

import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.dto.CategoryDto;

import java.util.List;

public interface CategoryRepositoryCustom {

    CategoryDto findCategoryById(String id);

    List<CategoryDto> findAllCategories();

    List<Category> findAncestors(Category category);

    List<Category> findCategoriesByAttribute(String attributeId);

    List<Category> findCategoriesByParent(String parentId);
}

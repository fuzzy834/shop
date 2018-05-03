package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.dto.CategoryCreationDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto findCategoryById(String lang, String id);

    List<CategoryDto> findAllCategories(String lang);

    Category createUpdateCategory(CategoryCreationDto categoryCreationDto);

    void deleteCategory(String categoryId, boolean deleteWithProducts);

    void deleteCategory(String categoryId, String replacementId);
}

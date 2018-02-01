package ua.home.stat_shop.persistence.repository;

import ua.home.stat_shop.persistence.dto.CategoryDto;

import java.util.List;

public interface CategoryRepositoryCustom {

    CategoryDto findCategoryById(String lang, String id);

    List<CategoryDto> findAllCategories(String lang);
}

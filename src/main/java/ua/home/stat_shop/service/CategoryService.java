package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto findCategoryById(String lang, String id);

    List<CategoryDto> findAllCategories(String lang);

}

package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.domain.Category;

import java.util.List;

public interface CategoryService {

    Category findCategoryByName(String name);

    Category findCategoryById(String id);

}

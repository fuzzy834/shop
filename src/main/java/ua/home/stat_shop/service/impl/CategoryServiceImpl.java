package ua.home.stat_shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findCategoryByName(String lang, String name) {
        return categoryRepository.findCategoryByName(lang, name);
    }

    @Override
    public Category findCategoryById(String lang, String id) {
        return categoryRepository.findOne(id);
    }

}

package ua.home.stat_shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto findCategoryById(String lang, String id) {
        return categoryRepository.findCategoryById(lang, id);
    }

    @Override
    public List<CategoryDto> findAllCategories(String lang) {
        return categoryRepository.findAllCategories(lang);
    }
}

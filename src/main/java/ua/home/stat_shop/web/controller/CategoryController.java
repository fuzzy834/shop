package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/{lang}/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(@PathVariable String lang) {

        return ResponseEntity.ok(categoryService.findAllCategories(lang));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String lang, @PathVariable String id) {

        return ResponseEntity.ok(categoryService.findCategoryById(lang, id));
    }
}

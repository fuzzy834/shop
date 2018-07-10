package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.dto.CategoryCreationDto;
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

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(@PathVariable String lang) {

        return ResponseEntity.ok(categoryService.findAllCategories(lang));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String lang, @PathVariable String id) {
        return ResponseEntity.ok(categoryService.findCategoryById(lang, id));
    }

    @DeleteMapping("/{id}/{deleteWithProducts}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id, @PathVariable boolean deleteWithProducts) {
        categoryService.deleteCategory(id, deleteWithProducts);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}/replace/{replacementId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id, @PathVariable String replacementId) {
        categoryService.deleteCategory(id, replacementId);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
        return ResponseEntity.ok(categoryService.createUpdateCategory(categoryCreationDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<Category> editCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
        return ResponseEntity.ok(categoryService.createUpdateCategory(categoryCreationDto));
    }
}

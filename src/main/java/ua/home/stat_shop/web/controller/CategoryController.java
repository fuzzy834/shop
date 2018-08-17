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
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @CrossOrigin
    @DeleteMapping("/{id}/{deleteWithProducts}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id, @PathVariable boolean deleteWithProducts) {
        categoryService.deleteCategory(id, deleteWithProducts);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{id}/replace/{replacementId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id, @PathVariable String replacementId) {
        categoryService.deleteCategory(id, replacementId);
        return ResponseEntity.ok(id);
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
        categoryService.createUpdateCategory(categoryCreationDto);
        return ResponseEntity.ok("");
    }

    @CrossOrigin
    @PutMapping("/edit")
    public ResponseEntity<String> editCategory(@RequestBody CategoryCreationDto categoryCreationDto) {
        categoryService.createUpdateCategory(categoryCreationDto);
        return ResponseEntity.ok("");
    }
}

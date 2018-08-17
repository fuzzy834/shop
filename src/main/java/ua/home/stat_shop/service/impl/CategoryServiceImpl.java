package ua.home.stat_shop.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.dto.*;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.CategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private AttributeRepository attributeRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, AttributeRepository attributeRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public CategoryDto findCategoryById(String id) {
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public void deleteCategory(String categoryId, boolean deleteWithProducts) {
        deleteCategory(categoryId);
        List<Product> products = productRepository.findProductByCategory(categoryId);
        if (deleteWithProducts) {
            products.forEach(product -> productRepository.delete(product.getId()));
        } else {
            products.forEach(product -> product.setCategory(null));
            productRepository.save(products);
        }
    }

    @Override
    public void deleteCategory(String categoryId, String replacementId) {
        deleteCategory(categoryId);
        Category category = categoryRepository.findOne(replacementId);
        ProductCategory productCategory = new ProductCategory(category);
        List<Product> products = productRepository.findProductByCategory(categoryId);
        products.forEach(product -> product.setCategory(productCategory));
        productRepository.save(products);
    }

    @Override
    public String createUpdateCategory(CategoryCreationDto categoryCreationDto) {
        Category parent;
        Category category;
        List<Product> products = Lists.newArrayList();
        if (Objects.isNull(categoryCreationDto.getCategoryId())) {
            category = new Category();
            category.setAttributes(new HashSet<>());
        } else {
            category = categoryRepository.findOne(categoryCreationDto.getCategoryId());
            products = productRepository.findProductByCategory(categoryCreationDto.getCategoryId());
        }
        if (Objects.nonNull(categoryCreationDto.getParent())) {
            parent = categoryRepository.findOne(categoryCreationDto.getParent());
            category.setSubCategory(true);
            category.setParent(parent);
        } else {
            category.setSubCategory(false);
        }
        FieldDto nameDto = categoryCreationDto.getName();
        if (nameDto.isTranslated()) {
            Map<String, String> localizedName = ((LocalizedFieldDto) nameDto).getLocalizedField();
            category.setCategoryName(new LocalizedField(localizedName));
        } else {
            String nonLocalizedName = ((NonLocalizedFieldDto) nameDto).getNonLocalizedField();
            category.setCategoryName(new NotLocalizedField(nonLocalizedName));
        }
        category.setAttributes(categoryCreationDto.getAttributes());
        if (!products.isEmpty()) {
            products = products.stream().peek(product -> product.setCategory(new ProductCategory(category)))
                    .collect(Collectors.toList());
            productRepository.save(products);
        }
        Category c = categoryRepository.save(category);
        return c.getId();
    }

    private void deleteCategory(String categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        Category parent = category.getParent();
        Set<String> attributes = category.getAttributes();
        Iterable<Attribute> attributeIterator = attributeRepository.findAll(attributes);
        attributeIterator.forEach(attribute ->
                attribute.getAttributeValues().forEach(value -> {
                    if (categoryId.equals(value.getCategoryId())) {
                        value.setCategoryId(null);
                    }
                })
        );
        categoryRepository.delete(categoryId);
        List<Category> children = categoryRepository.findCategoriesByParent(categoryId);
        children.forEach(child -> {
            if (Objects.nonNull(parent)) {
                child.setParent(parent);
            } else {
                child.setParent(null);
                child.setSubCategory(false);
            }
        });
        categoryRepository.save(children);
    }

//    private CategoryDto getCategoryDto(Category category) {
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setId(category.getId());
//        categoryDto.setParent(category.getParent().getId());
//        categoryDto.setAttributes(category.getAttributes());
//        CategoryName categoryName = category.getCategoryName();
//        if (categoryName instanceof LocalizedCategoryName) {
//            LocalizedCategoryName localizedCategoryName = (LocalizedCategoryName) categoryName;
//            categoryDto.setName(new LocalizedNameDto(localizedCategoryName.getLocalizedName()));
//        } else {
//            NotLocalizedCategoryName nonLocalizedCategoryName = (NotLocalizedCategoryName) categoryName;
//            categoryDto.setName(new NonLocalizedNameDto(nonLocalizedCategoryName.getNonLocalizedName()));
//        }
//        List<Category> children = categoryRepository.findChildren(category.getId());
//        if (!children.isEmpty()) {
//            categoryDto.setChildren(new HashSet<>());
//            for (Category child : children) {
//                CategoryDto childDto = getCategoryDto(child);
//                categoryDto.getChildren().add(childDto);
//            }
//        }
//
//        return categoryDto;
//    }
}

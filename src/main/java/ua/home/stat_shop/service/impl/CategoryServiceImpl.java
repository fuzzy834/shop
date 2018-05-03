package ua.home.stat_shop.service.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductCategory;
import ua.home.stat_shop.persistence.dto.CategoryCreationDto;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    public CategoryDto findCategoryById(String lang, String id) {
        return categoryRepository.findCategoryById(lang, id);
    }

    @Override
    public List<CategoryDto> findAllCategories(String lang) {
        return categoryRepository.findAllCategories(lang);
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
    public Category createUpdateCategory(CategoryCreationDto categoryCreationDto) {
        Category parent;
        Category category;
        List<Product> products = Lists.newArrayList();
        if (Objects.isNull(categoryCreationDto.getCategoryId())) {
            category = new Category();
        } else {
            category = categoryRepository.findOne(categoryCreationDto.getCategoryId());
            products = productRepository.findProductByCategory(categoryCreationDto.getCategoryId());
        }
        if (Objects.nonNull(categoryCreationDto.getParent())) {
            parent = categoryRepository.findOne(categoryCreationDto.getParent());
            category.setSubCategory(true);
            category.setParent(parent);
            Set<String> ancestors = Objects.isNull(parent.getAncestors()) ? Sets.newHashSet() : parent.getAncestors();
            ancestors.add(parent.getId());
            category.setAncestors(ancestors);

        } else {
            category.setSubCategory(false);
        }
        category.setLocalizedNames(categoryCreationDto.getLocalizedNames());
        if (!products.isEmpty()) {
            products = products.stream().peek(product -> product.setCategory(new ProductCategory(category)))
                    .collect(Collectors.toList());
            productRepository.save(products);
        }
        return categoryRepository.save(category);
    }

    private void deleteCategory(String categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        Category parent = category.getParent();
        Set<String> attributes = category.getAttributes().keySet();
        Iterable<Attribute> attributeIterator = attributeRepository.findAll(attributes);
        attributeIterator.forEach(attribute ->
                attribute.getAttributeValues().forEach(value -> {
                    if (categoryId.equals(value.getCategoryId())) {
                        value.setCategoryId(null);
                    }
                })
        );
        categoryRepository.delete(categoryId);
        List<Category> children = categoryRepository.findChildren(categoryId);
        children.forEach(child -> {
                    child.getAncestors().remove(categoryId);
                    String parentId = ((LazyLoadingProxy) child.getParent()).toDBRef().getId().toString();
                    if (categoryId.equals(parentId)) {
                        if (!category.getSubCategory()) {
                            child.setParent(null);
                            child.setSubCategory(false);
                        } else {
                            child.setParent(parent);
                        }
                    }
                }
        );
        categoryRepository.save(children);
    }
}

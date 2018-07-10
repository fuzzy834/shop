package ua.home.stat_shop.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;
import ua.home.stat_shop.persistence.domain.ProductCategory;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private AttributeRepository attributeRepository;

    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, AttributeRepository attributeRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto findAttributeById(String lang, String id) {
        return productRepository.findProductById(lang, id);
    }

    @Override
    public Page<ProductDto> findAll(String lang, Pageable pageable) {
        return productRepository.findAllProducts(lang, pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributes(String lang, Map<String, Set<String>> attributes, Pageable pageable) {
        return productRepository.findProductByAttributes(lang, attributes, pageable);
    }

    @Override
    public Page<ProductDto> findProductByCategory(String lang, String id, Pageable pageable) {
        return productRepository.findProductByCategory(lang, id, pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributesAndCategory(String lang, Map<String, Set<String>> attributes, String categoryId, Pageable pageable) {
        return productRepository.findProductByCategoryAndAttributes(lang, attributes, categoryId, pageable);
    }

    @Override
    public void addProductAttribute(String productId, String attributeId, Set<String> values) {
        productRepository.addProductAttribute(productId, attributeId, values);
    }

    @Override
    public void deleteProductAttribute(String productId, String attributeId) {
        productRepository.deleteProductAttribute(productId, attributeId);
    }

    @Override
    public Product createOrUpdateProduct(ProductCreationDto product) {
        List<Attribute> attributes = Lists.newArrayList(attributeRepository
                .findAll(product.getAttributeValueMap().keySet()));
        Set<ProductAttribute> productAttributes = attributes.stream().map
                (attribute -> new ProductAttribute(attribute, attribute.getAttributeValues().stream().filter(value ->
                        product.getAttributeValueMap().get(attribute.getId()).contains(value.getId()))
                        .collect(Collectors.toSet()))).collect(Collectors.toSet());
        Category category = categoryRepository.findOne(product.getCategory());
        category.getAttributes().forEach((k, v) -> {
            if (category.getAttributes().containsKey(k)) {
                Integer oldValue = category.getAttributes().get(k);
                category.getAttributes().replace(k, oldValue, ++oldValue);
            } else {
                category.getAttributes().put(k, 1);
            }
        });
        categoryRepository.save(category);
        ProductCategory productCategory = new ProductCategory(category);
        Product result = new Product(product.getProductBase(), productCategory, productAttributes);
        if (Objects.nonNull(product.getProductId())) {
            result.setId(product.getProductId());
        }
        return productRepository.save(result);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findOne(productId);
        Category category = categoryRepository.findOne(product.getCategory().getCategoryId());
        product.getAttributes().forEach(productAttribute -> {
            Integer oldValue = category.getAttributes().get(productAttribute.getAttributeId());
            category.getAttributes().replace(productAttribute.getAttributeId(), oldValue, --oldValue);
        });
        categoryRepository.save(category);
        productRepository.delete(product);
    }
}

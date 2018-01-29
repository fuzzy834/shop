package ua.home.stat_shop.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {

    private AttributeRepository attributeRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(AttributeRepository attributeRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findProductByAttributes(List<String> attributes, List<String> values, Pageable pageable) {
        Map<String, String> ids = IntStream.range(0, attributes.size())
                .mapToObj(i -> Maps.immutableEntry(attributes.get(i), values.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.findProductByAttributes(ids, pageable);
    }

    @Override
    public Page<Product> findProductByCategory(String id, Pageable pageable) {
        return productRepository.findProductByCategory(id, pageable);
    }

    @Override
    public Page<Product> findProductByAttributesAndCategory(List<String> attributes, List<String> values, String categoryId, Pageable pageable) {
        Map<String, String> ids = IntStream.range(0, attributes.size())
                .mapToObj(i -> Maps.immutableEntry(attributes.get(i), values.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.findProductByCategoryAndAttributes(ids, categoryId, pageable);
    }

}

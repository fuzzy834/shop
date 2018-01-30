package ua.home.stat_shop.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductDto> findAll(String lang, Pageable pageable) {
        return productRepository.findAllProducts(lang, pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributes(String lang, List<String> attributes, List<String> values, Pageable pageable) {
        Map<String, String> ids = IntStream.range(0, attributes.size())
                .mapToObj(i -> Maps.immutableEntry(attributes.get(i), values.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.findProductByAttributes(lang, ids, pageable);
    }

    @Override
    public Page<ProductDto> findProductByCategory(String lang, String id, Pageable pageable) {
        return productRepository.findProductByCategory(lang, id, pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributesAndCategory(String lang, List<String> attributes, List<String> values, String categoryId, Pageable pageable) {
        Map<String, String> ids = IntStream.range(0, attributes.size())
                .mapToObj(i -> Maps.immutableEntry(attributes.get(i), values.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.findProductByCategoryAndAttributes(lang, ids, categoryId, pageable);
    }

}

package ua.home.stat_shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;
import java.util.Set;

public interface ProductService {

    ProductDto findAttributeById(String lang, String id);

    Page<ProductDto> findAll(String lang, Pageable pageable);

    Page<ProductDto> findProductByAttributes(String lang, Map<String, Set<String>> attributes, Pageable pageable);

    Page<ProductDto> findProductByCategory(String lang, String id, Pageable pageable);

    Page<ProductDto> findProductByAttributesAndCategory(String lang, Map<String, Set<String>> attributes, String categoryId, Pageable pageable);

    void addProductAttribute(String productId, String attributeId, Set<String> values);

    void deleteProductAttribute(String productId, String attributeId);

    Product createOrUpdateProduct(ProductCreationDto product);

    void deleteProduct(String productId);
}

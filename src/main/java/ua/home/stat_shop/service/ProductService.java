package ua.home.stat_shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    ProductDto findAttributeById(String lang, String id);

    Page<ProductDto> findAll(String lang, Pageable pageable);

    Page<ProductDto> findProductByAttributes(String lang, List<String> attributes, List<String> values, Pageable pageable);

    Page<ProductDto> findProductByCategory(String lang, String id, Pageable pageable);

    Page<ProductDto> findProductByAttributesAndCategory(String lang, List<String> attributes, List<String> values, String categoryId, Pageable pageable);

    void addProductAttribute(String productId, String attributeId, String valueId);

    void deleteProductAttribute(String productId, String attributeId);

    Product createOrUpdateProduct(ProductCreationDto product);

    void deleteProduct(String productId);
}

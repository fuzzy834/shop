package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.Map;

public interface ProductRepositoryCustom {

    ProductDto findProductById(String lang, String id);

    Page<ProductDto> findAllProducts(String lang, Pageable pageable);

    Page<ProductDto> findProductByCategory(String lang, String categoryId, Pageable pageable);

    Page<ProductDto> findProductByAttributes(String lang, Map<String, String> ids, Pageable pageable);

    Page<ProductDto> findProductByCategoryAndAttributes(String lang, Map<String, String> attributeIds, String categoryId, Pageable pageable);

    void addProductAttribute(String productId, String attributeId, String valueId);

    void addTemporalProductAttribute(String productId, String attributeId, String valueId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    void deleteProductAttribute(String productId, String attributeId);

    void changeAttributePriority(String productId, String attributeId, Integer priority);
}

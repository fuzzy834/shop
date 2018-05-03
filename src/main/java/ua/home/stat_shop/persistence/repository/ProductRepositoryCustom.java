package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.Discount;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    ProductDto findProductById(String lang, String id);

    Page<ProductDto> findAllProducts(String lang, Pageable pageable);

    Page<ProductDto> findProductByCategory(String lang, String categoryId, Pageable pageable);

    List<Product> findProductByCategory(String categoryId);

    Page<ProductDto> findProductByAttributes(String lang, Map<String, String> ids, Pageable pageable);

    Page<ProductDto> findProductByCategoryAndAttributes(String lang, Map<String, String> attributeIds, String categoryId, Pageable pageable);

    void addProductAttribute(String productId, String attributeId, String valueId);

    void deleteProductAttribute(String productId, String attributeId);

    void changeAttributePriority(String productId, String attributeId, Integer priority);

    void setProductDiscount(String productId, Discount discount);

    void deleteProductDiscount(String productId);

    List<Product> findProductByAttribute(String attributeId);
}

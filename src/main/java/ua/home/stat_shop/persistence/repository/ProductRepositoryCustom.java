package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.dto.ProductDto;

import java.util.Map;

public interface ProductRepositoryCustom {

    Page<ProductDto> findAllProducts(String lang, Pageable pageable);

    Page<ProductDto> findProductByCategory(String lang, String categoryId, Pageable pageable);

    Page<ProductDto> findProductByAttributes(String lang, Map<String, String> ids, Pageable pageable);

    Page<ProductDto> findProductByCategoryAndAttributes(String lang, Map<String, String> attributeIds, String categoryId, Pageable pageable);
}

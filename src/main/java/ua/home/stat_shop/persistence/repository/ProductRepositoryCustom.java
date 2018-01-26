package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductRepositoryCustom {

    Page<Product> findProductByCategory(String categoryId, Pageable pageable);

    Page<Product> findProductByAttributes(List<ProductAttribute> attributes, Pageable pageable);
}

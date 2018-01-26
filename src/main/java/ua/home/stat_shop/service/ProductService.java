package ua.home.stat_shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.ProductAttribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findProductByAttributes(List<ProductAttribute> attributes, Pageable pageable);

    Page<Product> findProductByCategory(String id, Pageable pageable);

    Page<Product> findProductByAttributesAndCategory(List<ProductAttribute> attributes, Category category, Pageable pageable);
}

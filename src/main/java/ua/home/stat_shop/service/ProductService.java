package ua.home.stat_shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;

import java.util.List;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findProductByAttributes(List<Attribute> attributes, Pageable pageable);

    Page<Product> findProductByCategory(Category category, Pageable pageable);

    Page<Product> findProductByAttributesAndCategory(List<Attribute> attributes, Category category, Pageable pageable);
}

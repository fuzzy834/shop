package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findProductByAttributes(List<Attribute> attributes, Pageable pageable);

    Page<Product> findProductByCategory(Category category, Pageable pageable);

    Page<Product> findProductByAttributesAndCategory(List<Attribute> attributes, Category category, Pageable pageable);
}

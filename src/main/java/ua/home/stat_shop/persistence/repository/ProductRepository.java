package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {

    //TODO: fix, not working
//    Page<Product> findProductByAttributes(List<ProductAttribute> attributes, Pageable pageable);

//    Page<Product> findProductByCategory(String id, Pageable pageable);

    //TODO: fix, not working
    Page<Product> findProductByAttributesAndCategory(List<ProductAttribute> attributes, Category category, Pageable pageable);
}

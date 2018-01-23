package ua.home.stat_shop.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    //TODO: fix, not working
    Page<Product> findProductByAttributes(List<Attribute> attributes, Pageable pageable);

    //TODO: test this query :
    //    @Query("{category: {$in: [{$match : {_id : ?0}}," +
    //            "{$graphLookup:" +
    //                "{" +
    //                    "from : 'category'," +
    //                    "startWith : '$_id'," +
    //                    "connectFromField : '_id'," +
    //                    "connectToField : 'parent'" +
    //                "}" +
    //            "}]}}")
    Page<Product> findProductByCategory(Category category, Pageable pageable);

    //TODO: fix, not working
    Page<Product> findProductByAttributesAndCategory(List<Attribute> attributes, Category category, Pageable pageable);
}

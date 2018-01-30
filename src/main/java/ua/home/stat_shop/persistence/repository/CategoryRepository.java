package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.home.stat_shop.persistence.domain.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{localizedNames.?0 : ?1}")
    Category findCategoryByName(String lang, String name);


}

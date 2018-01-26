package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.home.stat_shop.persistence.domain.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    @Query("{$where: 'function() {for(var lang in this.localizedNames) if(this.localizedNames[lang] == ?0){return this;}}'}")
    Category findCategoryByName(String name);


}

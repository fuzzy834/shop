package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
}

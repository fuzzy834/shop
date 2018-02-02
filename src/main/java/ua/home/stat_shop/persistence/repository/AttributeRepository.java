package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.Attribute;

public interface AttributeRepository extends MongoRepository<Attribute, String>, AttributeRepositoryCustom {
}

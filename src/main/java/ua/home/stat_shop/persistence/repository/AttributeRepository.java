package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.MultivaluedAttribute;

public interface AttributeRepository extends MongoRepository<MultivaluedAttribute, String> {
}

package ua.home.stat_shop.persistence.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.repository.ProductRepositoryCustom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private MongoTemplate mongoTemplate;

    @Autowired
    public ProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Product> findProductByCategory(String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Query query = Query.query(criteria.orOperator(firstOption, secondOption));
        List<Product> products = mongoTemplate.find(query, Product.class);
        return PageableExecutionUtils.getPage(products, pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<Product> findProductByAttributes(Map<String, String> ids, Pageable pageable) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(ids.entrySet().stream().map(attr -> Criteria.where("attributes")
                .elemMatch(Criteria.where("attributeId").is(attr.getKey()).and("value.id").is(attr.getValue())))
                .distinct().toArray(Criteria[]::new)));
        List<Product> products = mongoTemplate.find(query, Product.class);
        return PageableExecutionUtils.getPage(products, pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<Product> findProductByCategoryAndAttributes(Map<String, String> attributeIds, String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Criteria[] attributeCriteria = attributeIds.entrySet().stream().map(attr -> Criteria.where("attributes")
                .elemMatch(Criteria.where("attributeId").is(attr.getKey()).and("value.id").is(attr.getValue())))
                .distinct().toArray(Criteria[]::new);
        Criteria[] allCriteria = Arrays.copyOf(attributeCriteria, attributeCriteria.length + 1);
        allCriteria[attributeCriteria.length] = criteria.orOperator(firstOption, secondOption);
        Query query = Query.query(criteria.andOperator(allCriteria));
        List<Product> products = mongoTemplate.find(query, Product.class);
        return PageableExecutionUtils.getPage(products, pageable, () -> mongoTemplate.count(query, Product.class));
    }
}

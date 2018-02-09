package ua.home.stat_shop.persistence.repository.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.ProductRepositoryCustom;
import ua.home.stat_shop.persistence.tasks.Scheduler;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final List<String> productDtoNonI18nFields = ImmutableList.of(
            "_id", "attributes.priority", "attributes.name.nonLocalizedName", "attributes.attributeId",
            "attributes.value.nonLocalizedValue", "attributes.value.quantity",
            "attributes.temporal", "attributes.startDateTime", "endDateTime",
            "productBase.retailPrice", "productBase.bulkPrice", "productBase.currency"
    );

    private static final List<String> productDtoI18nFields = ImmutableList.of(
            "category.localizedNames.%s", "attributes.name.localizedName.%s", "attributes.value.localizedValue.%s",
            "productBase.localizedProductName.%s", "productBase.localizedProductDescription.%s"
    );

    private MongoTemplate mongoTemplate;

    @Autowired
    public ProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ProductDto findProductById(String lang, String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return getProductDtosFromQuery(query, lang).stream().findFirst().orElse(null);
    }

    @Override
    public Page<ProductDto> findAllProducts(String lang, Pageable pageable) {
        Query query = Query.query(Criteria.where("_class").is(Product.class.getName()));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query, lang), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<ProductDto> findProductByCategory(String lang, String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Query query = Query.query(criteria.orOperator(firstOption, secondOption));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query, lang), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<ProductDto> findProductByAttributes(String lang, Map<String, String> ids, Pageable pageable) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(ids.entrySet().stream().map(attr -> Criteria.where("attributes")
                .elemMatch(Criteria.where("attributeId").is(attr.getKey()).and("value.id").is(attr.getValue())))
                .distinct().toArray(Criteria[]::new)));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query, lang), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<ProductDto> findProductByCategoryAndAttributes(String lang, Map<String, String> attributeIds, String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Criteria[] attributeCriteria = attributeIds.entrySet().stream().map(attr -> Criteria.where("attributes")
                .elemMatch(Criteria.where("attributeId").is(attr.getKey()).and("value.id").is(attr.getValue())))
                .distinct().toArray(Criteria[]::new);
        Criteria[] allCriteria = Arrays.copyOf(attributeCriteria, attributeCriteria.length + 1);
        allCriteria[attributeCriteria.length] = criteria.orOperator(firstOption, secondOption);
        Query query = Query.query(criteria.andOperator(allCriteria));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query, lang), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public void addProductAttribute(String productId, String attributeId, String valueId) {
        Attribute attribute = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(attributeId)), Attribute.class);
        attribute.getAttributeValues().stream()
                .filter(value -> value.getId().equals(valueId)).findFirst().ifPresent(attributeValue -> {
            ProductAttribute productAttribute = new ProductAttribute(attribute, attributeValue);
            Product product = mongoTemplate.findAndModify(
                    Query.query(Criteria.where("_id").is(productId)),
                    new Update().addToSet("attributes", productAttribute),
                    Product.class
            );
            onProductAttributeListChanged(attributeId, product.getCategory(), 1);
        });
    }

    @Override
    public void addTemporalProductAttribute(String productId, String attributeId, String valueId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Attribute attribute = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(attributeId)), Attribute.class);
        attribute.getAttributeValues().stream()
                .filter(value -> value.getId().equals(valueId)).findFirst().ifPresent(attributeValue -> {
            ProductAttribute productAttribute = new ProductAttribute(startDateTime, endDateTime, attribute, attributeValue);
            Product product = mongoTemplate.findAndModify(
                    Query.query(Criteria.where("_id").is(productId)),
                    new Update().addToSet("attributes", productAttribute),
                    Product.class
            );
            onProductAttributeListChanged(attributeId, product.getCategory(), 1);
            Scheduler.scheduleTask(() -> deleteProductAttribute(productId, attributeId), endDateTime);
        });
    }

    @Override
    public void deleteProductAttribute(String productId, String attributeId) {
        Update update = new Update();
        Product product = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(productId)), Product.class);
        product.getAttributes().stream()
                .filter(attr -> attr.getAttributeId().equals(attributeId)).findFirst()
                .ifPresent(productAttribute -> {
                    mongoTemplate.updateMulti(
                            Query.query(Criteria.where("_id").is(productId)),
                            update.pull("attributes", productAttribute),
                            Product.class);
                    onProductAttributeListChanged(attributeId, product.getCategory(), -1);
                });
    }

    @Override
    public void changeAttributePriority(String productId, String attributeId, Integer priority) {
        Query query = Query.query(Criteria.where("_id").is(productId).and("attributes.attributeId").is(attributeId));
        mongoTemplate.upsert(query, new Update().set("attributes.$.priority", priority), Product.class);
    }

    private void onProductAttributeListChanged(String attributeId, ProductCategory productCategory, Integer inc) {
        if (Objects.isNull(attributeId)) return;
        Set<String> categories = productCategory.getAncestors();
        categories.add(productCategory.getCategoryId());
        Update update = new Update();
        update.inc("attributes." + attributeId, inc);
        mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(categories)), update, Category.class);
    }

    private void includeFields(Query query, String lang) {
        productDtoNonI18nFields.forEach(f -> query.fields().include(f));
        productDtoI18nFields.forEach(f -> query.fields().include(String.format(f, lang)));
    }

    private List<ProductDto> getProductDtosFromQuery(Query query, String lang) {
        includeFields(query, lang);
        List<ProductDto> productDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> productDtos.add(DbObjectToDto.getProductDto(dbObject, lang));
        mongoTemplate.executeQuery(query, "product", documentCallbackHandler);
        return productDtos;
    }
}

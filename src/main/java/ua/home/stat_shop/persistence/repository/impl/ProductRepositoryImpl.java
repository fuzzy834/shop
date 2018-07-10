package ua.home.stat_shop.persistence.repository.impl;

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
import ua.home.stat_shop.persistence.converters.DtoConstructor;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.AttributeValue;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Discount;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;
import ua.home.stat_shop.persistence.domain.ProductCategory;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.ProductRepositoryCustom;
import ua.home.stat_shop.persistence.tasks.Scheduler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private MongoTemplate mongoTemplate;
    private DtoConstructor dtoConstructor;

    @Autowired
    public ProductRepositoryImpl(MongoTemplate mongoTemplate, DtoConstructor dtoConstructor) {
        this.mongoTemplate = mongoTemplate;
        this.dtoConstructor = dtoConstructor;
    }

    @Override
    public ProductDto findProductById(String lang, String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return getProductDtosFromQuery(query).stream().findFirst().orElse(null);
    }

    @Override
    public Page<ProductDto> findAllProducts(String lang, Pageable pageable) {
        Query query = Query.query(Criteria.where("_class").is(Product.class.getName()));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<ProductDto> findProductByCategory(String lang, String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Query query = Query.query(criteria.orOperator(firstOption, secondOption));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public List<Product> findProductByCategory(String categoryId) {
        Query query = Query.query(Criteria.where("category.categoryId").is(categoryId));
        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public Page<ProductDto> findProductByAttributes(String lang, Map<String, Set<String>> ids, Pageable pageable) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(
                ids.entrySet().stream().distinct().map(attr -> Criteria.where("attributes")
                        .elemMatch(
                                Criteria.where("attributeId")
                                        .is(attr.getKey())
                                        .and("valueIds").all(attr.getValue()))
                ).toArray(Criteria[]::new)));
        return PageableExecutionUtils.getPage(
                getProductDtosFromQuery(query), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public Page<ProductDto> findProductByCategoryAndAttributes(String lang, Map<String, Set<String>> attributes, String categoryId, Pageable pageable) {
        Criteria criteria = new Criteria();
        Criteria firstOption = Criteria.where("category.categoryId").is(categoryId);
        Criteria secondOption = Criteria.where("category.ancestors").is(categoryId);
        Criteria[] attributeCriteria = attributes.entrySet().stream().map(attr -> Criteria.where("attributes")
                .elemMatch(Criteria.where("attributeId")
                        .is(attr.getKey())
                        .and("valuesIds").all(attr.getValue())))
                .distinct().toArray(Criteria[]::new);
        Criteria[] allCriteria = Arrays.copyOf(attributeCriteria, attributeCriteria.length + 1);
        allCriteria[attributeCriteria.length] = criteria.orOperator(firstOption, secondOption);
        Query query = Query.query(criteria.andOperator(allCriteria));
        return PageableExecutionUtils.getPage(getProductDtosFromQuery(query), pageable, () -> mongoTemplate.count(query, Product.class));
    }

    @Override
    public void addProductAttribute(String productId, String attributeId, Set<String> values) {
        Attribute attribute = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(attributeId)), Attribute.class);
        Set<AttributeValue> attributeValues = attribute.getAttributeValues().stream()
                .filter(value -> values.contains(value.getId())).collect(Collectors.toSet());
        ProductAttribute productAttribute = new ProductAttribute(attribute, attributeValues);
        Product product = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(productId)),
                new Update().addToSet("attributes", productAttribute),
                Product.class
        );
        onProductAttributeListChanged(attributeId, product.getCategory(), 1);
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

    @Override
    public void setProductDiscount(String productId, Discount discount) {
        if (Objects.nonNull(discount.getStart())) {
            Scheduler.scheduleTask(() -> mongoTemplate.upsert(Query.query(Criteria.where("_id").is(productId)),
                    new Update().set("discount", discount), Product.class), discount.getStart());
        } else {
            mongoTemplate.upsert(Query.query(Criteria.where("_id").is(productId)),
                    new Update().set("discount", discount), Product.class);
        }
        if (Objects.nonNull(discount.getEnd())) {
            Scheduler.scheduleTask(() -> deleteProductDiscount(productId), discount.getEnd());
        }
    }

    @Override
    public void deleteProductDiscount(String productId) {
        mongoTemplate.upsert(Query.query(Criteria.where("_id").is(productId)),
                new Update().unset("discount"), Product.class);
    }

    @Override
    public List<Product> findProductByAttribute(String attributeId) {
        Query query = Query.query(Criteria.where("attributes").elemMatch(Criteria.where("attributeId").is(attributeId)));
        return mongoTemplate.find(query, Product.class);
    }

    private void onProductAttributeListChanged(String attributeId, ProductCategory productCategory, Integer inc) {
        if (Objects.isNull(attributeId)) return;
        Set<String> categories = productCategory.getAncestors();
        categories.add(productCategory.getCategoryId());
        Update update = new Update();
        update.inc("attributes." + attributeId, inc);
        mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(categories)), update, Category.class);
    }

    private List<ProductDto> getProductDtosFromQuery(Query query) {
        includeFields(query);
        List<ProductDto> productDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> productDtos.add(DbObjectToDto.getProductDto(dbObject));
        mongoTemplate.executeQuery(query, "product", documentCallbackHandler);
        return productDtos;
    }

    private void includeFields(Query query) {
        dtoConstructor.getIncludedFields(ProductDto.class)
                .forEach(f -> query.fields().include(f));
    }
}

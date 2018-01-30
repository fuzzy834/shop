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
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.ProductRepositoryCustom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private static final List<String> productDtoNonI18nFields = ImmutableList.of(
            "_id", "retailPrice", "bulkPrice", "attributes.name.nonLocalizedName", "attributes.value.nonLocalizedValue"
    );

    private static final List<String> productDtoI18nFields = ImmutableList.of(
            "category.localizedNames.%s", "attributes.name.localizedName.%s", "attributes.value.localizedValue.%s"
    );

    private MongoTemplate mongoTemplate;

    @Autowired
    public ProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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

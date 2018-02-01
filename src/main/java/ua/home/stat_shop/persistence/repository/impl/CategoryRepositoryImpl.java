package ua.home.stat_shop.persistence.repository.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.CategoryRepositoryCustom;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private static final List<String> categoryDtoNonI18nFields = ImmutableList.of(
            "_id", "parent.$id", "subCategory"
    );

    private static final List<String> categoryDtoI18nFields = ImmutableList.of(
            "localizedNames.%s"
    );

    private MongoTemplate mongoTemplate;

    @Autowired
    public CategoryRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CategoryDto findCategoryById(String lang, String id) {
        Criteria criteria = new Criteria();
        Query query = Query.query(criteria.orOperator(Criteria.where("_id").is(id), Criteria.where("ancestors").is(id)));
        List<CategoryDto> categoryDtoList = getCategoryDtosFromQuery(query, lang);
        if (categoryDtoList.size() == 1) {
            return categoryDtoList.get(0);
        }
        return groupByParent(categoryDtoList, Collections.singleton(id)).stream().findFirst().orElse(null);
    }

    @Override
    public List<CategoryDto> findAllCategories(String lang) {
        Query query = Query.query(Criteria.where("_class").is(Category.class.getName()));
        List<CategoryDto> categoryDtoLists = getCategoryDtosFromQuery(query, lang);
        Set<String> roots = categoryDtoLists.stream().filter(categoryDto -> Objects.isNull(categoryDto.getParent()))
                .map(CategoryDto::getId).collect(Collectors.toSet());
        return groupByParent(categoryDtoLists, roots);
    }

    private List<CategoryDto> getCategoryDtosFromQuery(Query query, String lang) {
        includeFields(query, lang);
        List<CategoryDto> categoryDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> categoryDtos.add(DbObjectToDto.getCategoryDto(dbObject, lang));
        mongoTemplate.executeQuery(query, "category", documentCallbackHandler);
        return categoryDtos;
    }

    private void includeFields(Query query, String lang) {
        categoryDtoNonI18nFields.forEach(f -> query.fields().include(f));
        categoryDtoI18nFields.forEach(f -> query.fields().include(String.format(f, lang)));
    }

    private List<CategoryDto> groupByParent(List<CategoryDto> categoryDtoList, Set<String> roots) {
        Map<String, CategoryDto> categories = categoryDtoList.stream()
                .map(category -> Maps.immutableEntry(category.getId(), category))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Set<CategoryDto>> children = categoryDtoList.stream()
                .filter(category -> Objects.nonNull(category.getParent()))
                .collect(Collectors.groupingBy(CategoryDto::getParent, Collectors.toSet()));

        List<CategoryDto> result = children.entrySet().stream().map(entry -> {
            CategoryDto category = categories.get(entry.getKey());
            category.getChildren().addAll(entry.getValue());
            return category;
        }).filter(category -> roots.contains(category.getId())).collect(Collectors.toList());

        roots.forEach(root -> {
            if (!children.containsKey(root)) {
                result.add(categories.get(root));
            }
        });

        return result;
    }
}

package ua.home.stat_shop.persistence.repository.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.converters.DtoConstructor;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.CategoryRepositoryCustom;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private MongoTemplate mongoTemplate;
    private DtoConstructor dtoConstructor;

    @Autowired
    public CategoryRepositoryImpl(MongoTemplate mongoTemplate, DtoConstructor dtoConstructor) {
        this.mongoTemplate = mongoTemplate;
        this.dtoConstructor = dtoConstructor;
    }

    @Override
    public CategoryDto findCategoryById(String id) {
        Query query = Query.query(Criteria.where("_class").is(Category.class.getName()));
        List<CategoryDto> categoryDtoLists = getCategoryDtosFromQuery(query);
        CategoryDto category = categoryDtoLists.stream()
                .filter(categoryDto -> categoryDto.getId().equals(id))
                .findFirst().orElse(null);
        if (Objects.nonNull(category)) {
            return groupByParent(categoryDtoLists, Collections.singleton(category.getId())).get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        Query query = Query.query(Criteria.where("_class").is(Category.class.getName()));
        List<CategoryDto> categoryDtoLists = getCategoryDtosFromQuery(query);
        Set<String> roots = categoryDtoLists.stream().filter(categoryDto -> Objects.isNull(categoryDto.getParent()))
                .map(CategoryDto::getId).collect(Collectors.toSet());
        return groupByParent(categoryDtoLists, roots);
    }

    @Override
    public List<Category> findAncestors(Category category) {
        List<Category> ancestors = new LinkedList<>();
        Category parent = category.getParent();
        while (Objects.nonNull(parent)) {
            ancestors.add(parent);
            parent = parent.getParent();
        }
        return ancestors;
    }

    @Override
    public List<Category> findCategoriesByAttribute(String attributeId) {
        Query query = Query.query(Criteria.where("attributes." + attributeId).exists(true));
        return mongoTemplate.find(query, Category.class);
    }

    @Override
    public List<Category> findCategoriesByParent(String parentId) {
        Query query = Query.query(Criteria.where("parent.$id").is(new ObjectId(parentId)));
        return mongoTemplate.find(query, Category.class);
    }

    private List<CategoryDto> getCategoryDtosFromQuery(Query query) {
        includeFields(query);
        List<CategoryDto> categoryDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> categoryDtos.add(DbObjectToDto.getCategoryDto(dbObject));
        mongoTemplate.executeQuery(query, "category", documentCallbackHandler);
        return categoryDtos;
    }

    private void includeFields(Query query) {
        dtoConstructor.getIncludedFields(CategoryDto.class).forEach(f -> query.fields().include(f));
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

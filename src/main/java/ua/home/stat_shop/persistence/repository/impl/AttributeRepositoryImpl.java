package ua.home.stat_shop.persistence.repository.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.converters.DtoConstructor;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.repository.AttributeRepositoryCustom;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AttributeRepositoryImpl implements AttributeRepositoryCustom {

    private DtoConstructor dtoConstructor;

    private MongoTemplate mongoTemplate;

    @Autowired
    public AttributeRepositoryImpl(MongoTemplate mongoTemplate, DtoConstructor dtoConstructor) {
        this.mongoTemplate = mongoTemplate;
        this.dtoConstructor = dtoConstructor;
    }

    @Override
    public List<AttributeDto> findAllAttributes() {
        Query query = Query.query(Criteria.where("_class").is(Attribute.class.getName()));
        return getAttributeDtosFromQuery(query);
    }

    @Override
    public AttributeDto findAttributeById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return getAttributeDtosFromQuery(query).stream().findFirst().orElse(null);
    }

    @Override
    public List<AttributeDto> findAttributeByIds(Set<String> ids) {
        Query query = Query.query(Criteria.where("_id").in(ids));
        return getAttributeDtosFromQuery(query);
    }

    @Override
    public List<AttributeDto> findAttributesByCategory(String categoryId) {
        Query categoryQuery = Query.query(Criteria.where("_id").is(categoryId));
        categoryQuery.fields().include("attributes");
        categoryQuery.fields().exclude("_id");
        Set<String> ids = Sets.newHashSet();
        mongoTemplate.executeQuery(categoryQuery, "category", (dbObject) -> {
            BasicDBObject attributes = (BasicDBObject) dbObject.get("attributes");
            ids.addAll(attributes.entrySet().stream().filter(entry -> (Integer) entry.getValue() > 0)
                    .map(Map.Entry::getKey).collect(Collectors.toSet()));
        });
        Query attributeQuery = Query.query(Criteria.where("_id").in(ids));
        return getAttributeDtosFromQuery(attributeQuery);
    }

    private List<AttributeDto> getAttributeDtosFromQuery(Query query) {
        includeFields(query);
        List<AttributeDto> attributeDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> attributeDtos.add(DbObjectToDto.getAttributeDto(dbObject));
        mongoTemplate.executeQuery(query, "attribute", documentCallbackHandler);
        return attributeDtos;
    }

    private void includeFields(Query query) {
        dtoConstructor.getIncludedFields(AttributeDto.class)
                .forEach(f -> query.fields().include(f));
    }
}

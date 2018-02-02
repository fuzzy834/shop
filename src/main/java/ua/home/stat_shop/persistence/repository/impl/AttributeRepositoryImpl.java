package ua.home.stat_shop.persistence.repository.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.converters.DbObjectToDto;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.repository.AttributeRepositoryCustom;

import java.util.List;
import java.util.Set;

@Component
public class AttributeRepositoryImpl implements AttributeRepositoryCustom{

    private static final List<String> attributeDtoNonI18nFields = ImmutableList.of(
            "_id", "filterOn", "attributeName.nonLocalizedName", "attributeValues._id", "attributeValues.nonLocalizedValue"
    );

    private static final List<String> attributeDtoI18nFields = ImmutableList.of(
            "attributeName.localizedName.%s", "attributeValues.localizedValue.%s"
    );

    private MongoTemplate mongoTemplate;

    @Autowired
    public AttributeRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<AttributeDto> findAllAttributes(String lang) {
        Query query = Query.query(Criteria.where("_class").is(Attribute.class.getName()));
        return getAttributeDtosFromQuery(query, lang);
    }

    @Override
    public AttributeDto findAttributeById(String lang, String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return getAttributeDtosFromQuery(query, lang).stream().findFirst().orElse(null);
    }

    @Override
    public List<AttributeDto> findAttributeByIds(String lang, Set<String> ids) {
        Query query = Query.query(Criteria.where("_id").in(ids));
        return getAttributeDtosFromQuery(query, lang);
    }

    private List<AttributeDto> getAttributeDtosFromQuery(Query query, String lang) {
        includeFields(query, lang);
        List<AttributeDto> attributeDtos = Lists.newArrayList();
        DocumentCallbackHandler documentCallbackHandler = (dbObject) -> attributeDtos.add(DbObjectToDto.getAttributeDto(dbObject, lang));
        mongoTemplate.executeQuery(query, "attribute", documentCallbackHandler);
        return attributeDtos;
    }

    private void includeFields(Query query, String lang) {
        attributeDtoNonI18nFields.forEach(f -> query.fields().include(f));
        attributeDtoI18nFields.forEach(f -> query.fields().include(String.format(f, lang)));
    }
}

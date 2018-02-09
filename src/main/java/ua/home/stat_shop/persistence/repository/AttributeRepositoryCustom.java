package ua.home.stat_shop.persistence.repository;

import ua.home.stat_shop.persistence.dto.AttributeDto;

import java.util.List;
import java.util.Set;

public interface AttributeRepositoryCustom {

    List<AttributeDto> findAllAttributes(String lang);

    AttributeDto findAttributeById(String lang, String id);

    List<AttributeDto> findAttributeByIds(String lang, Set<String> ids);

    List<AttributeDto> findAttributesByCategory(String lang, String categoryId);
}

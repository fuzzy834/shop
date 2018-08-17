package ua.home.stat_shop.persistence.repository;

import ua.home.stat_shop.persistence.dto.AttributeDto;

import java.util.List;
import java.util.Set;

public interface AttributeRepositoryCustom {

    List<AttributeDto> findAllAttributes();

    AttributeDto findAttributeById(String id);

    List<AttributeDto> findAttributeByIds(Set<String> ids);

    List<AttributeDto> findAttributesByCategory(String categoryId);
}

package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.dto.AttributeDto;

import java.util.List;
import java.util.Set;

public interface AttributeService {

    AttributeDto findAttributeById(String id);

    List<AttributeDto> findAttributesByIds(Set<String> ids);

    List<AttributeDto> findAllAttributes();

    void deleteAttribute(String attributeId);
}

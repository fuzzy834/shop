package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.dto.AttributeDto;

import java.util.List;
import java.util.Set;

public interface AttributeService {

    AttributeDto findAttributeById(String lang, String id);

    List<AttributeDto> findAttributesByIds(String lang, Set<String> ids);

    List<AttributeDto> findAllAttributes(String lang);

    void deleteAttribute(String attributeId);
}

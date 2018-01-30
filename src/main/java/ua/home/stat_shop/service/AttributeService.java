package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.domain.Attribute;

import java.util.List;

public interface AttributeService {

    Attribute findAttributeById(String lang,  String id);

    List<Attribute> findAttributesByIds(String lang, List<String> ids);
}

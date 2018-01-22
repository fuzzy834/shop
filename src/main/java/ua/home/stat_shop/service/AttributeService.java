package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.domain.MultivaluedAttribute;

import java.util.List;

public interface AttributeService {

    MultivaluedAttribute findAttributeById(String id);

    List<MultivaluedAttribute> findAttributesByIds(List<String> ids);
}

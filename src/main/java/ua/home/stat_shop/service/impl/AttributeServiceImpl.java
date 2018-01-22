package ua.home.stat_shop.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.MultivaluedAttribute;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.service.AttributeService;

import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {

    private AttributeRepository attributeRepository;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public MultivaluedAttribute findAttributeById(String id) {
        return attributeRepository.findOne(id);
    }

    @Override
    public List<MultivaluedAttribute> findAttributesByIds(List<String> ids) {
        return Lists.newArrayList(attributeRepository.findAll(ids));
    }
}

package ua.home.stat_shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.service.AttributeService;

import java.util.List;
import java.util.Set;

@Service
public class AttributeServiceImpl implements AttributeService {

    private AttributeRepository attributeRepository;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Override
    public AttributeDto findAttributeById(String lang, String id) {
        return attributeRepository.findAttributeById(lang, id);
    }

    @Override
    public List<AttributeDto> findAttributesByIds(String lang, Set<String> ids) {
        return attributeRepository.findAttributeByIds(lang, ids);
    }

    @Override
    public List<AttributeDto> findAllAttributes(String lang) {
        return attributeRepository.findAllAttributes(lang);
    }
}

package ua.home.stat_shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.AttributeService;

import java.util.List;
import java.util.Set;

@Service
public class AttributeServiceImpl implements AttributeService {

    private AttributeRepository attributeRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public AttributeServiceImpl(AttributeRepository attributeRepository, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.attributeRepository = attributeRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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

    @Override
    public void deleteAttribute(String attributeId) {
        attributeRepository.delete(attributeId);
        List<Product> products = productRepository.findProductByAttribute(attributeId);
        products.forEach(product -> product.getAttributes().stream()
                .filter(productAttribute -> productAttribute.getAttributeId().equals(attributeId))
                .forEachOrdered(productAttribute -> product.getAttributes().remove(productAttribute)));
        List<Category> categories = categoryRepository.findCategoriesByAttribute(attributeId);
        categories.forEach(category -> category.getAttributes().remove(attributeId));
        productRepository.save(products);
        categoryRepository.save(categories);
    }
}

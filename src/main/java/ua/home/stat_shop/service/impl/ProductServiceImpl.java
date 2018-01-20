package ua.home.stat_shop.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.ProductService;

import javax.annotation.PostConstruct;

@Service
public class ProductServiceImpl implements ProductService {

    private AttributeRepository attributeRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(AttributeRepository attributeRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        MultivaluedAttribute notLocalizedAttribute = new MultivaluedAttribute(
                "ololo",
                ImmutableSet.of(
                        "tototo",
                        "tatata",
                        "tititi"
                )
        );

        MultivaluedAttribute localizedNamesAttribute = new MultivaluedAttribute(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "hahaha",
                        LangCodes.URK.getCode(), "hohoho",
                        LangCodes.RUS.getCode(), "hihihi"
                ),
                ImmutableSet.of(
                        "bububu",
                        "bababa",
                        "bibibi"
                )
        );

        MultivaluedAttribute localizedNamesAndValuesAtrribute = new MultivaluedAttribute(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "dedede",
                        LangCodes.URK.getCode(), "dadada",
                        LangCodes.RUS.getCode(), "dododo"
                ),
                ImmutableList.of(
                        ImmutableMap.of(
                                LangCodes.EN.getCode(), "rororo",
                                LangCodes.URK.getCode(), "rarara",
                                LangCodes.RUS.getCode(), "rerere"
                        ),
                        ImmutableMap.of(
                                LangCodes.EN.getCode(), "sesese",
                                LangCodes.URK.getCode(), "sasasa",
                                LangCodes.RUS.getCode(), "sososo"
                        ),
                        ImmutableMap.of(
                                LangCodes.EN.getCode(), "xexexe",
                                LangCodes.URK.getCode(), "xaxaxa",
                                LangCodes.RUS.getCode(), "xixixi"
                        )
                )
        );

        Attribute attribute1 = new Attribute(notLocalizedAttribute, "tototo");
        Attribute attribute2 = new Attribute(localizedNamesAttribute, "bababa");
        Attribute attribute3 = new Attribute(localizedNamesAndValuesAtrribute, "xixixi");


        Category category = new Category("cacaca",
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "cococo",
                        LangCodes.URK.getCode(), "cecece",
                        LangCodes.RUS.getCode(), "cicici"
                )
        );

        Product product = new Product(100500d, 100499d, category,
                ImmutableSet.of(
                        attribute1,
                        attribute2,
                        attribute3
                )
        );

        attributeRepository.save(
                ImmutableSet.of(
                        notLocalizedAttribute,
                        localizedNamesAttribute,
                        localizedNamesAndValuesAtrribute
                )
        );
        categoryRepository.save(category);
        productRepository.save(product);
    }
}

package ua.home.stat_shop.persistence.bootstrap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.constants.LangCodes;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.MultivaluedAttribute;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;

@Component
public class ApplicationInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;
    private AttributeRepository attributeRepository;
    private ProductRepository productRepository;

    @Autowired
    public ApplicationInitListener(CategoryRepository categoryRepository, AttributeRepository attributeRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initDbData();
    }

    private void initDbData() {
        categoryRepository.deleteAll();
        attributeRepository.deleteAll();
        productRepository.deleteAll();

        MultivaluedAttribute notLocalizedAttribute = new MultivaluedAttribute(
                "ololo",
                ImmutableSet.of(
                        "tototo",
                        "tatata",
                        "tititi"
                ), true
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
                ), true
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
                ), true
        );

        Attribute attribute1 = new Attribute(notLocalizedAttribute, "tototo");
        Attribute attribute2 = new Attribute(localizedNamesAttribute, "bababa");
        Attribute attribute3 = new Attribute(localizedNamesAndValuesAtrribute, "xixixi");


        Category category = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category One",
                        LangCodes.URK.getCode(), "Категорія Один",
                        LangCodes.RUS.getCode(), "Категория Один"
                )
        );

        Product product = new Product(100500d, 100499d, category,
                ImmutableSet.of(
                        attribute1,
                        attribute2,
                        attribute3
                )
        );

        Product product1 = new Product(100501d, 100491d, category, ImmutableSet.of(
                attribute1,
                attribute2,
                attribute3
        ));

        attributeRepository.save(
                ImmutableSet.of(
                        notLocalizedAttribute,
                        localizedNamesAttribute,
                        localizedNamesAndValuesAtrribute
                )
        );
        categoryRepository.save(category);
        productRepository.save(product);
        productRepository.save(product1);
    }
}

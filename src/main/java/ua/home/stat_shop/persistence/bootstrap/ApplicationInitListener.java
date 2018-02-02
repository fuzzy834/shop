package ua.home.stat_shop.persistence.bootstrap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.constants.LangCodes;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;

import java.util.List;

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

        Attribute notLocalizedAttribute = new Attribute(
                new NotLocalizedAttributeName("ololo"),
                ImmutableSet.of(
                        new NotLocalizedAttributeValue("tototo"),
                        new NotLocalizedAttributeValue("tatata"),
                        new NotLocalizedAttributeValue("tititi")
                ), true
        );

        Attribute localizedNamesAttribute = new Attribute(
                new LocalizedAttributeName(ImmutableMap.of(
                        LangCodes.EN.getCode(), "hahaha",
                        LangCodes.URK.getCode(), "hohoho",
                        LangCodes.RUS.getCode(), "hihihi"
                )),
                ImmutableSet.of(
                        new NotLocalizedAttributeValue("bububu"),
                        new NotLocalizedAttributeValue("bababa"),
                        new NotLocalizedAttributeValue("bibibi")
                ), true
        );

        Attribute localizedNamesAndValuesAtrribute = new Attribute(
                new LocalizedAttributeName(ImmutableMap.of(
                        LangCodes.EN.getCode(), "dedede",
                        LangCodes.URK.getCode(), "dadada",
                        LangCodes.RUS.getCode(), "dododo"
                )),
                ImmutableSet.of(
                        new LocalizedAttributeValue(ImmutableMap.of(
                                LangCodes.EN.getCode(), "rororo",
                                LangCodes.URK.getCode(), "rarara",
                                LangCodes.RUS.getCode(), "rerere"
                        )),
                        new LocalizedAttributeValue(ImmutableMap.of(
                                LangCodes.EN.getCode(), "sesese",
                                LangCodes.URK.getCode(), "sasasa",
                                LangCodes.RUS.getCode(), "sososo"
                        )),
                        new LocalizedAttributeValue(ImmutableMap.of(
                                LangCodes.EN.getCode(), "xexexe",
                                LangCodes.URK.getCode(), "xaxaxa",
                                LangCodes.RUS.getCode(), "xixixi"
                        ))
                ), true
        );

        List<Attribute> attributes = attributeRepository.save(
                ImmutableSet.of(
                        notLocalizedAttribute,
                        localizedNamesAttribute,
                        localizedNamesAndValuesAtrribute
                )
        );

        ProductAttribute attribute1 = new ProductAttribute(notLocalizedAttribute, notLocalizedAttribute.getAttributeValues().iterator().next());
        ProductAttribute attribute2 = new ProductAttribute(localizedNamesAttribute, localizedNamesAttribute.getAttributeValues().iterator().next());
        ProductAttribute attribute3 = new ProductAttribute(localizedNamesAndValuesAtrribute, localizedNamesAndValuesAtrribute.getAttributeValues().iterator().next());

        Category categoryParent = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Zero",
                        LangCodes.URK.getCode(), "Категорія Нуль",
                        LangCodes.RUS.getCode(), "Категория Ноль"
                )
        );

        categoryParent = categoryRepository.save(categoryParent);

        Category category = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category One",
                        LangCodes.URK.getCode(), "Категорія Один",
                        LangCodes.RUS.getCode(), "Категория Один"
                ), categoryParent
        );

        categoryRepository.save(category);

        Category category1 = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Two",
                        LangCodes.URK.getCode(), "Категорія Два",
                        LangCodes.RUS.getCode(), "Категория Два"
                ), category
        );

        categoryRepository.save(category1);

        Category category2 = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Three",
                        LangCodes.URK.getCode(), "Категорія Три",
                        LangCodes.RUS.getCode(), "Категория Три"
                ), category
        );

        categoryRepository.save(category2);

        Category category3 = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Four",
                        LangCodes.URK.getCode(), "Категорія Чотири",
                        LangCodes.RUS.getCode(), "Категория Четыре"
                ), category
        );

        categoryRepository.save(category3);

        Category separateCategory = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Separate Category",
                        LangCodes.URK.getCode(), "Окрема Категорія",
                        LangCodes.RUS.getCode(), "Отдельная Категория"
                )
        );

        categoryRepository.save(separateCategory);

        Category separateCategory1 = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Separate Category 1",
                        LangCodes.URK.getCode(), "Окрема Категорія 1",
                        LangCodes.RUS.getCode(), "Отдельная Категория 1"
                ), separateCategory
        );

        categoryRepository.save(separateCategory1);

        Product product = new Product(100500d, 100499d, new ProductCategory(category3),
                ImmutableSet.of(
                        attribute1,
                        attribute2,
                        attribute3
                )
        );

        Product product1 = new Product(100501d, 100491d, new ProductCategory(category), ImmutableSet.of(
                attribute1,
                attribute2,
                attribute3
        ));

        productRepository.save(product);
        productRepository.save(product1);
    }
}

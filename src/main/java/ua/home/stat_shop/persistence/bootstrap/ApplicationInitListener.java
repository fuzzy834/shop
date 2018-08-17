package ua.home.stat_shop.persistence.bootstrap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.constants.Constants;
import ua.home.stat_shop.persistence.constants.LangCodes;
import ua.home.stat_shop.persistence.converters.DtoConstructor;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.LanguageRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ApplicationInitListener {

    private CategoryRepository categoryRepository;
    private AttributeRepository attributeRepository;
    private ProductRepository productRepository;
    private LanguageRepository languageRepository;
    private DtoConstructor dtoConstructor;

    @Autowired
    public ApplicationInitListener(CategoryRepository categoryRepository,
                                   AttributeRepository attributeRepository,
                                   ProductRepository productRepository,
                                   LanguageRepository languageRepository,
                                   DtoConstructor dtoConstructor) {
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
        this.productRepository = productRepository;
        this.languageRepository = languageRepository;
        this.dtoConstructor = dtoConstructor;
    }

    @EventListener
    private void initDbData(ContextRefreshedEvent contextRefreshedEvent) {

        categoryRepository.deleteAll();
        attributeRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.deleteAll();
        languageRepository.deleteAll();

        languageRepository.save(new Language(Constants.FALLBACK_LOCALE));
        LocaleContextHolder.setLocale(Constants.FALLBACK_LOCALE);

        Field attributeName1 = new NotLocalizedField("Колір");
        Attribute notLocalizedAttribute = new Attribute(
                attributeName1, ImmutableSet.of(
                new AttributeValue(new NotLocalizedField("Синій")),
                new AttributeValue(new NotLocalizedField("Чорний")),
                new AttributeValue(new NotLocalizedField("Червоний"))
        )
        );

        Field attributeName2 = new LocalizedField(ImmutableMap.of(
                LangCodes.EN.getCode(), "Type",
                LangCodes.UKR.getCode(), "Тип",
                LangCodes.RUS.getCode(), "Тип"
        ));

        Attribute localizedNamesAttribute = new Attribute(
                attributeName2, ImmutableSet.of(
                new AttributeValue(new NotLocalizedField("bububu")),
                new AttributeValue(new NotLocalizedField("bababa")),
                new AttributeValue(new NotLocalizedField("bibibi"))
        )
        );

        AttributeValue attributeValueWithQuantity = new AttributeValue(new LocalizedField(ImmutableMap.of(
                LangCodes.EN.getCode(), "rororo",
                LangCodes.UKR.getCode(), "rarara",
                LangCodes.RUS.getCode(), "rerere"
        )));
        attributeValueWithQuantity.setQuantity(20);

        Field attributeName3 = new LocalizedField(ImmutableMap.of(
                LangCodes.EN.getCode(), "dedede",
                LangCodes.UKR.getCode(), "dadada",
                LangCodes.RUS.getCode(), "dododo"
        ));

        Attribute localizedNamesAndValuesAtrribute = new Attribute(
                attributeName3, ImmutableSet.of(
                attributeValueWithQuantity,
                new AttributeValue(new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "sesese",
                        LangCodes.UKR.getCode(), "sasasa",
                        LangCodes.RUS.getCode(), "sososo"
                ))),
                new AttributeValue(new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "xexexe",
                        LangCodes.UKR.getCode(), "xaxaxa",
                        LangCodes.RUS.getCode(), "xixixi"
                )))
        )
        );

        List<Attribute> attributes = attributeRepository.save(
                ImmutableSet.of(
                        notLocalizedAttribute,
                        localizedNamesAttribute,
                        localizedNamesAndValuesAtrribute
                )
        );

        Set<String> attributeIds = attributes.stream().map(Attribute::getId).collect(Collectors.toSet());

        ProductAttribute attribute1 = new ProductAttribute(notLocalizedAttribute, notLocalizedAttribute.getAttributeValues());
        ProductAttribute attribute2 = new ProductAttribute(localizedNamesAttribute, localizedNamesAttribute.getAttributeValues());
        ProductAttribute attribute3 = new ProductAttribute(localizedNamesAndValuesAtrribute, localizedNamesAndValuesAtrribute.getAttributeValues());

        Category categoryParent = new Category(new LocalizedField(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Zero",
                        LangCodes.UKR.getCode(), "Категорія Нуль",
                        LangCodes.RUS.getCode(), "Категория Ноль"
                ))
        );
        categoryParent.setAttributes(attributeIds);
        categoryParent = categoryRepository.save(categoryParent);

        Category category = new Category(new LocalizedField(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category One",
                        LangCodes.UKR.getCode(), "Категорія Один",
                        LangCodes.RUS.getCode(), "Категория Один"
                )), categoryParent
        );
        category.setAttributes(attributeIds);
        categoryRepository.save(category);

        Category category1 = new Category(new NotLocalizedField("Not Localized Category 1"),
                category);

        category1.setAttributes(attributeIds);
        categoryRepository.save(category1);

        Category category2 = new Category(new NotLocalizedField("Not Localized Category 2"),
                category);

        category2.setAttributes(attributeIds);
        categoryRepository.save(category2);

        Category category3 = new Category(new LocalizedField(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Four",
                        LangCodes.UKR.getCode(), "Категорія Чотири",
                        LangCodes.RUS.getCode(), "Категория Четыре"
                )), category
        );

        category3.setAttributes(attributeIds);
        categoryRepository.save(category3);

        Category separateCategory = new Category(new LocalizedField(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Separate Category",
                        LangCodes.UKR.getCode(), "Окрема Категорія",
                        LangCodes.RUS.getCode(), "Отдельная Категория"
                ))
        );
        separateCategory.setAttributes(attributeIds);
        categoryRepository.save(separateCategory);

        Category separateCategory1 = new Category(new LocalizedField(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Separate Category 1",
                        LangCodes.UKR.getCode(), "Окрема Категорія 1",
                        LangCodes.RUS.getCode(), "Отдельная Категория 1"
                )), separateCategory
        );

        separateCategory1.setAttributes(attributeIds);
        categoryRepository.save(separateCategory1);

        ProductBase productBase = new ProductBase(
                new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "product1",
                        LangCodes.UKR.getCode(), "продукт1",
                        LangCodes.RUS.getCode(), "продукт1"
                )),
                new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "product1",
                        LangCodes.UKR.getCode(), "продукт1",
                        LangCodes.RUS.getCode(), "продукт1"
                )),
                1000d,
                9999d,
                "UAH"
        );

        ProductBase productBase1 = new ProductBase(
                new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "product2",
                        LangCodes.UKR.getCode(), "продукт2",
                        LangCodes.RUS.getCode(), "продукт2"
                )),
                new LocalizedField(ImmutableMap.of(
                        LangCodes.EN.getCode(), "product2",
                        LangCodes.UKR.getCode(), "продукт2",
                        LangCodes.RUS.getCode(), "продукт2"
                )),
                1000d,
                9999d,
                "UAH"
        );

        Product product = new Product(productBase, new ProductCategory(category3),
                ImmutableSet.of(
                        attribute2
                )
        );

        Product product1 = new Product(productBase1, new ProductCategory(category), ImmutableSet.of(
                attribute1,
                attribute2,
                attribute3
        ));

        Discount discount1 = new Discount();
        discount1.setPercentOff(5);
        Discount discount2 = new Discount();
        discount2.setPercentOff(10);
        discount2.setStart(LocalDateTime.now());
        discount2.setEnd(LocalDateTime.now().plus(Period.ofDays(10)));

        product.setDiscount(discount1);
        product1.setDiscount(discount2);

        product = productRepository.save(product);
        product1 = productRepository.save(product1);

        ProductAttribute attr = product.getAttributes().iterator().next();
        ProductAttribute attr1 = product1.getAttributes().iterator().next();
        productRepository.addProductAttribute(product.getId(), attr.getAttributeId(),
                attr1.getValues().stream().map(AttributeValue::getId).collect(Collectors.toSet()));
        productRepository.addProductAttribute(product.getId(), attr1.getAttributeId(),
                attr1.getValues().stream().map(AttributeValue::getId).collect(Collectors.toSet()));
        productRepository.changeAttributePriority(product.getId(), attribute2.getAttributeId(), 25);
    }
}

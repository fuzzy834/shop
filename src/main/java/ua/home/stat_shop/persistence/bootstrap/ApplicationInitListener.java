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
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.AttributeName;
import ua.home.stat_shop.persistence.domain.AttributeValue;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.Discount;
import ua.home.stat_shop.persistence.domain.Language;
import ua.home.stat_shop.persistence.domain.LocalizedAttributeName;
import ua.home.stat_shop.persistence.domain.LocalizedAttributeValue;
import ua.home.stat_shop.persistence.domain.NotLocalizedAttributeName;
import ua.home.stat_shop.persistence.domain.NotLocalizedAttributeValue;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.domain.ProductAttribute;
import ua.home.stat_shop.persistence.domain.ProductBase;
import ua.home.stat_shop.persistence.domain.ProductCategory;
import ua.home.stat_shop.persistence.dto.CategoryDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.LanguageRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
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

        AttributeName attributeName1 = new NotLocalizedAttributeName("ololo");
        attributeName1.setPriority(3);
        Attribute notLocalizedAttribute = new Attribute(
                attributeName1, ImmutableSet.of(
                new NotLocalizedAttributeValue("tototo"),
                new NotLocalizedAttributeValue("tatata"),
                new NotLocalizedAttributeValue("tititi")
        )
        );

        AttributeName attributeName2 = new LocalizedAttributeName(ImmutableMap.of(
                LangCodes.EN.getCode(), "hahaha",
                LangCodes.UKR.getCode(), "hohoho",
                LangCodes.RUS.getCode(), "hihihi"
        ));
        attributeName2.setPriority(2);
        Attribute localizedNamesAttribute = new Attribute(
                attributeName2, ImmutableSet.of(
                new NotLocalizedAttributeValue("bububu"),
                new NotLocalizedAttributeValue("bababa"),
                new NotLocalizedAttributeValue("bibibi")
        )
        );

        AttributeValue attributeValueWithQuantity = new LocalizedAttributeValue(ImmutableMap.of(
                LangCodes.EN.getCode(), "rororo",
                LangCodes.UKR.getCode(), "rarara",
                LangCodes.RUS.getCode(), "rerere"
        ));
        attributeValueWithQuantity.setQuantity(20);

        AttributeName attributeName3 = new LocalizedAttributeName(ImmutableMap.of(
                LangCodes.EN.getCode(), "dedede",
                LangCodes.UKR.getCode(), "dadada",
                LangCodes.RUS.getCode(), "dododo"
        ));
        attributeName3.setPriority(1);
        Attribute localizedNamesAndValuesAtrribute = new Attribute(
                attributeName3, ImmutableSet.of(
                attributeValueWithQuantity,
                new LocalizedAttributeValue(ImmutableMap.of(
                        LangCodes.EN.getCode(), "sesese",
                        LangCodes.UKR.getCode(), "sasasa",
                        LangCodes.RUS.getCode(), "sososo"
                )),
                new LocalizedAttributeValue(ImmutableMap.of(
                        LangCodes.EN.getCode(), "xexexe",
                        LangCodes.UKR.getCode(), "xaxaxa",
                        LangCodes.RUS.getCode(), "xixixi"
                ))
        )
        );

        List<Attribute> attributes = attributeRepository.save(
                ImmutableSet.of(
                        notLocalizedAttribute,
                        localizedNamesAttribute,
                        localizedNamesAndValuesAtrribute
                )
        );

        ProductAttribute attribute1 = new ProductAttribute(notLocalizedAttribute, notLocalizedAttribute.getAttributeValues());
        ProductAttribute attribute2 = new ProductAttribute(localizedNamesAttribute, localizedNamesAttribute.getAttributeValues());
        ProductAttribute attribute3 = new ProductAttribute(localizedNamesAndValuesAtrribute, localizedNamesAndValuesAtrribute.getAttributeValues());

        Category categoryParent = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Zero",
                        LangCodes.UKR.getCode(), "Категорія Нуль",
                        LangCodes.RUS.getCode(), "Категория Ноль"
                )
        );

        categoryParent = categoryRepository.save(categoryParent);

        Category category = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category One",
                        LangCodes.UKR.getCode(), "Категорія Один",
                        LangCodes.RUS.getCode(), "Категория Один"
                ), categoryParent
        );

        categoryRepository.save(category);

        Category category1 = new Category(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "Category Two",
                        LangCodes.UKR.getCode(), "Категорія Два",
                        LangCodes.RUS.getCode(), "Категория Два"
                ), category
        );

        categoryRepository.save(category1);

        Category category2 = new Category(
                ImmutableMap.of(
//                        LangCodes.EN.getCode(), "Category Three",
                        LangCodes.UKR.getCode(), "Категорія Три",
                        LangCodes.RUS.getCode(), "Категория Три"
                ), category
        );

        categoryRepository.save(category2);

        Category category3 = new Category(
                ImmutableMap.of(
//                        LangCodes.EN.getCode(), "Category Four",
                        LangCodes.UKR.getCode(), "Категорія Чотири",
                        LangCodes.RUS.getCode(), "Категория Четыре"
                ), category
        );

        categoryRepository.save(category3);

        Category separateCategory = new Category(
                ImmutableMap.of(
//                        LangCodes.EN.getCode(), "Separate Category",
                        LangCodes.UKR.getCode(), "Окрема Категорія",
                        LangCodes.RUS.getCode(), "Отдельная Категория"
                )
        );

        categoryRepository.save(separateCategory);

        Category separateCategory1 = new Category(
                ImmutableMap.of(
//                        LangCodes.EN.getCode(), "Separate Category 1",
                        LangCodes.UKR.getCode(), "Окрема Категорія 1",
                        LangCodes.RUS.getCode(), "Отдельная Категория 1"
                ), separateCategory
        );

        categoryRepository.save(separateCategory1);

        ProductBase productBase = new ProductBase(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "product1",
                        LangCodes.UKR.getCode(), "продукт1",
                        LangCodes.RUS.getCode(), "продукт1"
                ),
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "product1",
                        LangCodes.UKR.getCode(), "продукт1",
                        LangCodes.RUS.getCode(), "продукт1"
                ),
                1000d,
                9999d,
                "UAH"
        );

        ProductBase productBase1 = new ProductBase(
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "product2",
                        LangCodes.UKR.getCode(), "продукт2",
                        LangCodes.RUS.getCode(), "продукт2"
                ),
                ImmutableMap.of(
                        LangCodes.EN.getCode(), "product2",
                        LangCodes.UKR.getCode(), "продукт2",
                        LangCodes.RUS.getCode(), "продукт2"
                ),
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

package ua.home.stat_shop.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ua.home.stat_shop.persistence.constants.Constants;
import ua.home.stat_shop.persistence.domain.*;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.persistence.repository.AttributeRepository;
import ua.home.stat_shop.persistence.repository.CategoryRepository;
import ua.home.stat_shop.persistence.repository.ProductRepository;
import ua.home.stat_shop.service.ProductService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private AttributeRepository attributeRepository;

    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, AttributeRepository attributeRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto findAttributeById(String id) {
        return productRepository.findProductById(id);
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributes(Map<String, Set<String>> attributes, Pageable pageable) {
        return productRepository.findProductByAttributes(attributes, pageable);
    }

    @Override
    public Page<ProductDto> findProductByCategory(String id, Pageable pageable) {
        return productRepository.findProductByCategory(id, pageable);
    }

    @Override
    public Page<ProductDto> findProductByAttributesAndCategory(Map<String, Set<String>> attributes, String categoryId, Pageable pageable) {
        return productRepository.findProductByCategoryAndAttributes(attributes, categoryId, pageable);
    }

    @Override
    public void addProductAttribute(String productId, String attributeId, Set<String> values) {
        productRepository.addProductAttribute(productId, attributeId, values);
    }

    @Override
    public void deleteProductAttribute(String productId, String attributeId) {
        productRepository.deleteProductAttribute(productId, attributeId);
    }

    @Override
    public Product createOrUpdateProduct(ProductCreationDto product) {
        List<Attribute> attributes = Lists.newArrayList(attributeRepository
                .findAll(product.getAttributeValueMap().keySet()));
        Set<ProductAttribute> productAttributes = attributes.stream().map
                (attribute -> new ProductAttribute(attribute, attribute.getAttributeValues().stream().filter(value ->
                        product.getAttributeValueMap().get(attribute.getId()).contains(value.getId()))
                        .collect(Collectors.toSet()))).collect(Collectors.toSet());
        Category category = categoryRepository.findOne(product.getCategory());
        ProductCategory productCategory = new ProductCategory(category);
        Product result = new Product(product.getProductBase(), productCategory, productAttributes);
        Set<Image> images = Stream.of(product.getImages())
                .filter(Objects::nonNull)
                .map(this::transferProductImage)
                .collect(Collectors.toSet());
        if (Objects.nonNull(product.getProductId())) {
            result.setId(product.getProductId());
            result.getImages().stream()
                    .filter(image ->
                            images.stream().noneMatch(img -> img.getPath().equals(image.getPath()))
                    ).forEach(image -> {
                            boolean deleted = Paths.get(image.getPath()).toFile().delete();
                            System.out.println(deleted);
                        }
                    );
        }

        result.setImages(images);
        return productRepository.save(result);
    }

    private Image transferProductImage(CommonsMultipartFile file) {
        URL imagesUrl = getClass().getClassLoader().getResource(Constants.IMAGES_FOLDER);
        if (Objects.nonNull(imagesUrl)) {
            String imagesPath = imagesUrl.getPath();
            String imagePath = imagesPath + File.separator + file.getName();
            try {
                if (Files.notExists(Paths.get(imagePath))) {
                    file.transferTo(new File(imagePath));
                    Image image = new Image();
                    image.setPath(imagePath);
                    image.setSize(file.getSize());
                    image.setType(file.getContentType());
                    return image;
                }
                return null;
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findOne(productId);
        productRepository.delete(product);
    }
}

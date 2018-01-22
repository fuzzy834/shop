package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.home.stat_shop.persistence.domain.Attribute;
import ua.home.stat_shop.persistence.domain.Category;
import ua.home.stat_shop.persistence.domain.MultivaluedAttribute;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.service.AttributeService;
import ua.home.stat_shop.service.CategoryService;
import ua.home.stat_shop.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private AttributeService attributeService;
    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, AttributeService attributeService, CategoryService categoryService) {
        this.productService = productService;
        this.attributeService = attributeService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<Product>> getProductsByCategory (@PageableDefault Pageable pageable, @PathVariable String id) {
        Category category = categoryService.findCategoryById(id);
        Page<Product> products = productService.findProductByCategory(category, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/attributes")
    public ResponseEntity<Page<Product>> getProductsByAttributes (@PageableDefault Pageable pageable,
                                                                  @RequestParam List<String> ids,
                                                                  @RequestParam List<String> values) {
        List<MultivaluedAttribute> multivaluedAttributes = attributeService.findAttributesByIds(ids);
        List<Attribute> attributes = IntStream.range(0, multivaluedAttributes.size())
                .mapToObj(i -> new Attribute(multivaluedAttributes.get(i), values.get(i)))
                .collect(Collectors.toList());
        Page<Product> products = productService.findProductByAttributes(attributes, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}/attributes")
    public ResponseEntity<Page<Product>> getProductsByCategoryAndAttributes (@PageableDefault Pageable pageable,
                                                                             @PathVariable String id,
                                                                             @RequestParam List<String> ids,
                                                                             @RequestParam List<String> values) {
        ids.forEach(System.out::println);
        values.forEach(System.out::println);
        List<MultivaluedAttribute> multivaluedAttributes = attributeService.findAttributesByIds(ids);
        List<Attribute> attributes = IntStream.range(0, multivaluedAttributes.size())
                .mapToObj(i -> new Attribute(multivaluedAttributes.get(i), values.get(i)))
                .collect(Collectors.toList());
        Category category = categoryService.findCategoryById(id);
        Page<Product> products = productService.findProductByAttributesAndCategory(attributes, category, pageable);
        return ResponseEntity.ok(products);
    }
}

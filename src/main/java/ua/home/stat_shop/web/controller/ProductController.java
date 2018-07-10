package ua.home.stat_shop.web.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/{lang}/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PathVariable String lang,
                                                           @PageableDefault Pageable pageable) {

        return ResponseEntity.ok(productService.findAll(lang, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable String lang,
                                                  @PathVariable String id) {

        return ResponseEntity.ok(productService.findAttributeById(lang, id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategory(@PathVariable String lang,
                                                                  @PathVariable String id,
                                                                  @PageableDefault Pageable pageable) {

        Page<ProductDto> products = productService.findProductByCategory(lang, id, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/attributes")
    public ResponseEntity<Page<ProductDto>> getProductsByAttributes(HttpServletRequest request,
                                                                    @PathVariable String lang,
                                                                    @PageableDefault Pageable pageable) {

        Map<String, Set<String>> attributes = request.getParameterMap().entrySet().stream()
                .filter(param ->
                        !param.getKey().equals("page")
                                || !param.getKey().equals("sort")
                                || !param.getKey().equals("size")
                ).map(attribute ->
                        Maps.immutableEntry(
                                attribute.getKey(),
                                Stream.of(attribute.getValue())
                                        .flatMap(val -> Stream.of(val.split(",")))
                                        .collect(Collectors.toSet())
                        )
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Page<ProductDto> products = productService.findProductByAttributes(lang, attributes, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}/attributes")
    public ResponseEntity<Page<ProductDto>> getProductsByCategoryAndAttributes(HttpServletRequest request,
                                                                               @PathVariable String lang,
                                                                               @PathVariable String id,
                                                                               @PageableDefault Pageable pageable) {

        Map<String, Set<String>> attributes = request.getParameterMap().entrySet().stream()
                .filter(param ->
                        !param.getKey().equals("page")
                                || !param.getKey().equals("sort")
                                || !param.getKey().equals("size")
                ).map(attribute ->
                        Maps.immutableEntry(
                                attribute.getKey(),
                                Stream.of(attribute.getValue())
                                        .flatMap(val -> Stream.of(val.split(",")))
                                        .collect(Collectors.toSet())
                        )
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Page<ProductDto> products = productService.findProductByAttributesAndCategory(lang, attributes, id, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreationDto productCreationDto) {
        return ResponseEntity.ok(productService.createOrUpdateProduct(productCreationDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<Product> editProduct(@RequestBody ProductCreationDto productCreationDto) {
        return ResponseEntity.ok(productService.createOrUpdateProduct(productCreationDto));
    }
}

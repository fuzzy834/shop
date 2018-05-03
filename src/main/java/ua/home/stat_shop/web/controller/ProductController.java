package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.persistence.dto.ProductCreationDto;
import ua.home.stat_shop.persistence.dto.ProductDto;
import ua.home.stat_shop.service.ProductService;

import java.util.List;

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
    public ResponseEntity<Page<ProductDto>> getProductsByAttributes(@PathVariable String lang,
                                                                    @RequestParam List<String> attributes,
                                                                    @RequestParam List<String> values,
                                                                    @PageableDefault Pageable pageable) {

        Page<ProductDto> products = productService.findProductByAttributes(lang, attributes, values, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}/attributes")
    public ResponseEntity<Page<ProductDto>> getProductsByCategoryAndAttributes(@PathVariable String lang,
                                                                               @PathVariable String id,
                                                                               @RequestParam List<String> attributes,
                                                                               @RequestParam List<String> values,
                                                                               @PageableDefault Pageable pageable) {

        Page<ProductDto> products = productService.findProductByAttributesAndCategory(lang, attributes, values, id, pageable);
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

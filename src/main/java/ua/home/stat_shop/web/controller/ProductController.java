package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.home.stat_shop.persistence.domain.Product;
import ua.home.stat_shop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault Pageable pageable) {

        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PageableDefault Pageable pageable, @PathVariable String id) {

        Page<Product> products = productService.findProductByCategory(id, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/attributes")
    public ResponseEntity<Page<Product>> getProductsByAttributes(@PageableDefault Pageable pageable,
                                                                 @RequestParam List<String> attributes,
                                                                 @RequestParam List<String> values) {

        Page<Product> products = productService.findProductByAttributes(attributes, values, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}/attributes")
    public ResponseEntity<Page<Product>> getProductsByCategoryAndAttributes(@PageableDefault Pageable pageable,
                                                                            @PathVariable String id,
                                                                            @RequestParam List<String> attributes,
                                                                            @RequestParam List<String> values) {

        Page<Product> products = productService.findProductByAttributesAndCategory(attributes, values, id, pageable);
        return ResponseEntity.ok(products);
    }
}

package ua.home.stat_shop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.home.stat_shop.persistence.dto.AttributeDto;
import ua.home.stat_shop.service.AttributeService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/{lang}/attributes")
public class AttributeController {

    private AttributeService attributeService;

    @Autowired
    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<AttributeDto>> getAllAttributes(@PathVariable String lang,
                                                               @RequestParam(value = "attributes", required = false) Set<String> attributes) {

        if (null == attributes) {
            return ResponseEntity.ok(attributeService.findAllAttributes(lang));
        } else {
            return ResponseEntity.ok(attributeService.findAttributesByIds(lang, attributes));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDto> getAttributesById(@PathVariable String lang,
                                                          @PathVariable String id) {

        return ResponseEntity.ok(attributeService.findAttributeById(lang, id));
    }
}

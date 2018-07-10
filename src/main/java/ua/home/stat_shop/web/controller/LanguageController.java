package ua.home.stat_shop.web.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.home.stat_shop.persistence.constants.Constants;
import ua.home.stat_shop.persistence.converters.UTF8ResourceBundleControl;
import ua.home.stat_shop.persistence.domain.Language;
import ua.home.stat_shop.service.LanguageService;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/language")
public class LanguageController {

    private LanguageService languageService;

    @Value("${available.locales}")
    private String availableLocales;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<Language> getCurrentLanguage() {
        String currentLocale = LocaleContextHolder.getLocale().toLanguageTag();
        Language currentLanguage = languageService.findLanguageByCode(currentLocale);
        return ResponseEntity.ok(currentLanguage);
    }

    @CrossOrigin
    @GetMapping("/all")
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.findAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @CrossOrigin
    @GetMapping("/locales")
    public ResponseEntity<List<Language>> getAvailableLocales() {
        Locale[] locales = Stream.of(availableLocales.split(","))
                .map(Locale::forLanguageTag).toArray(Locale[]::new);
        List<Language> languages = Stream.of(locales).map(Language::new)
                .filter(language -> !StringUtils.isEmpty(language.getLanguageName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(languages);
    }

    @CrossOrigin
    @GetMapping("/{lang}")
    public ResponseEntity<Language> switchLanguage(@PathVariable String lang) {
        Language language = languageService.findLanguageByCode(lang);
        languageService.switchLanguage(language);
        return ResponseEntity.ok(language);
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<Language> addLanguage(@RequestBody Language language) {
        Language newLanguage = languageService.addLanguage(language.getLanguageCode());
        return ResponseEntity.ok(newLanguage);
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteLanguage(@PathVariable String id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping(value = "/i18n/{code}")
    public ResponseEntity<Map<String, String>> getI18n(@PathVariable String code) {
        ResourceBundle.Control utf8Control = new UTF8ResourceBundleControl();
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_BUNDLE_BASE_NAME,
                Locale.forLanguageTag(code), utf8Control);
        Map<String, String> i18n = bundle.keySet().stream()
                .map(key -> Maps.immutableEntry(key, bundle.getString(key)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return ResponseEntity.ok(i18n);
    }
}

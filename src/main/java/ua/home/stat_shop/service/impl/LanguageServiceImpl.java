package ua.home.stat_shop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.home.stat_shop.persistence.domain.Language;
import ua.home.stat_shop.persistence.repository.LanguageRepository;
import ua.home.stat_shop.service.LanguageService;

import java.util.List;
import java.util.Locale;

@Service
public class LanguageServiceImpl implements LanguageService {

    private LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language findLanguageById(String id) {
        return languageRepository.findOne(id);
    }

    @Override
    public Language findLanguageByCode(String code) {
        return languageRepository.findByLanguageCode(code);
    }

    @Override
    public Language findLanguageByName(String name) {
        return languageRepository.findByLanguageName(name);
    }

    @Override
    public void deleteLanguage(String id) {
        languageRepository.delete(id);
    }

    @Override
    public List<Language> findAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public Language addLanguage(String code, boolean defaultLanguage) {
        Locale locale = Locale.forLanguageTag(code);
        Language language = new Language(locale);
        language.setDefaultLanguage(defaultLanguage);
        return languageRepository.save(language);
    }

    @Override
    public Language addLanguage(String code) {
        return addLanguage(code, false);
    }

    @Override
    public Language getDefaultLanguage() {
        return languageRepository.getFirstByDefaultLanguage(true);
    }

    @Override
    public void switchLanguage(Language language) {
        LocaleContextHolder.setLocale(language.getLocale());
    }

    @Override
    public boolean languageExists(String name) {
        return languageRepository.existsByLanguageName(name);
    }
}

package ua.home.stat_shop.service;

import ua.home.stat_shop.persistence.domain.Language;

import java.util.List;

public interface LanguageService {

    List<Language> findAllLanguages();

    Language findLanguageById(String id);

    Language findLanguageByCode(String code);

    Language findLanguageByName(String name);

    Language getDefaultLanguage();

    Language addLanguage(String code);

    Language addLanguage(String code, boolean defaultLanguage);

    void deleteLanguage(String id);

    void switchLanguage(Language language);

    boolean languageExists(String name);
}

package ua.home.stat_shop.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.home.stat_shop.persistence.domain.Language;

public interface LanguageRepository extends MongoRepository<Language, String> {

    Language findByLanguageCode(String code);

    Language findByLanguageName(String name);

    Language getFirstByDefaultLanguage(boolean defaultLanguage);

    boolean existsByLanguageName(String name);
}

package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Language {

    @Id
    String id;
    @JsonProperty("code")
    String languageCode;
    @JsonProperty("name")
    String languageName;
    @JsonIgnore
    Locale locale;
    @JsonIgnore
    boolean defaultLanguage = false;

    public Language(Locale locale) {
        this.locale = locale;
        this.languageCode = locale.toLanguageTag();
        String displayName = locale.getDisplayLanguage(locale);
        this.languageName = StringUtils.capitalize(displayName);
    }
}

package ua.home.stat_shop.persistence.constants;

import lombok.Getter;

@Getter
public enum LangCodes {

    UKR("uk-UA"), RUS("ru-RU"), EN("en-US");

    private String code;

    LangCodes(String code) {
        this.code = code;
    }
}

package ua.home.stat_shop.persistence.constants;

import lombok.Getter;

@Getter
public enum LangCodes {

    UKR("ukr"), RUS("rus"), EN("en");

    private String code;

    LangCodes(String code) {
        this.code = code;
    }
}

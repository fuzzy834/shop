package ua.home.stat_shop.persistence.domain;

import lombok.Getter;

@Getter
public enum LangCodes {

    URK("ukr"), RUS("rus"), EN("en");

    private String code;

    LangCodes(String code) {
        this.code = code;
    }
}

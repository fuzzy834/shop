package ua.home.stat_shop.persistence.constants;

import lombok.Getter;

@Getter
public enum AttributeType {

    NOT_LOCALIZED("not_localized"),
    LOCALIZED_NAMES("localized_names"),
    LOCALIZED_NAMES_AND_VALUES("localized_names_and_values");

    private String type;

    AttributeType(String type) {
        this.type = type;
    }
}

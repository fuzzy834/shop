package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.home.stat_shop.persistence.constants.AttributeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductAttribute {

    private String attributeId;
    private String name;
    private String value;
    private String type;
    private Map<String, String> localizedNames;
    private Map<String, String> localizedValues;

    public ProductAttribute(Attribute attribute, String value) {
        this.attributeId = attribute.getId();
        this.name = attribute.getName();
        this.type = attribute.getType();
        if (this.type.equals(AttributeType.NOT_LOCALIZED.getType())) {
            this.value = getNonLocalizedValue(value, attribute.getValues());
        } else if (this.type.equals(AttributeType.LOCALIZED_NAMES.getType())) {
            this.localizedNames = attribute.getLocalizedNames();
            this.value = getNonLocalizedValue(value, attribute.getValues());
        } else {
            this.localizedNames = attribute.getLocalizedNames();
            this.localizedValues = getLocalizedValue(value, attribute.getLocalizedValues());
        }
    }

    private String getNonLocalizedValue(String value, Set<String> values) {
        return values.stream().filter(v -> v.equals(value))
                .findFirst().orElse(null);
    }

    private Map<String, String> getLocalizedValue(String value, List<Map<String, String>> values) {
        return values.stream()
                .filter(valMap -> valMap.entrySet().stream()
                        .anyMatch(entry -> entry.getValue().equals(value)))
                .findFirst().orElse(null);
    }
}

package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Attribute {

    private String name;
    private String value;
    private String type;
    private Map<String, String> localizedNames;
    private Map<String, String> localizedValues;

    public Attribute(MultivaluedAttribute multivaluedAttribute, String value) {
        this.name = multivaluedAttribute.getName();
        this.type = multivaluedAttribute.getType();
        if (this.type.equals(AttributeType.NOT_LOCALIZED.getType())) {
            this.value = multivaluedAttribute.getValues()
                    .stream().filter(v -> v.equals(value))
                    .findFirst().orElse(null);
        } else if (this.type.equals(AttributeType.LOCALIZED_NAMES.getType())) {
            this.localizedNames = multivaluedAttribute.getLocalizedNames();
            this.value = multivaluedAttribute.getValues()
                    .stream().filter(v -> v.equals(value))
                    .findFirst().orElse(null);
        } else {
            this.localizedNames = multivaluedAttribute.getLocalizedNames();
            this.localizedValues = multivaluedAttribute.getLocalizedValues().stream()
                    .filter(valMap -> valMap.entrySet().stream()
                            .anyMatch(entry -> entry.getValue().equals(value)))
                    .findFirst().orElse(null);
        }
    }
}

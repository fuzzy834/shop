package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class MultivaluedAttribute {

    @Id
    private String id;
    private String name;
    private String type;
    private Set<String> values;
    private Map<String, String> localizedNames;
    private List<Map<String, String>> localizedValues;

    public MultivaluedAttribute(String name, Set<String> values) {
        this.name = name;
        this.values = values;
        this.type = AttributeType.NOT_LOCALIZED.getType();
    }

    public MultivaluedAttribute(Map<String, String> localizedNames, Set<String> values) {
        this.name = localizedNames.entrySet().iterator().next().getValue();
        this.values = values;
        this.type = AttributeType.LOCALIZED_NAMES.getType();
        this.localizedNames = localizedNames;
    }

    public MultivaluedAttribute(Map<String, String> localizedNames, List<Map<String, String>> localizedValues) {
        this.name = localizedNames.entrySet().iterator().next().getValue();
        this.type = AttributeType.LOCALIZED_NAMES_AND_VALUES.getType();
        this.localizedNames = localizedNames;
        this.localizedValues = localizedValues;
    }
}



package ua.home.stat_shop.persistence.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.home.stat_shop.persistence.constants.AttributeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultivaluedAttribute {

    @Id
    private String id;
    private String name;
    private String type;
    private Boolean filterOn;
    private Set<String> values;
    private Map<String, String> localizedNames;
    private List<Map<String, String>> localizedValues;

    public MultivaluedAttribute(String name, Set<String> values, Boolean filterOn) {
        this.name = name;
        this.values = values;
        this.type = AttributeType.NOT_LOCALIZED.getType();
        this.filterOn = filterOn;
    }

    public MultivaluedAttribute(Map<String, String> localizedNames, Set<String> values, Boolean filterOn) {
        this.name = localizedNames.entrySet().iterator().next().getValue();
        this.values = values;
        this.type = AttributeType.LOCALIZED_NAMES.getType();
        this.localizedNames = localizedNames;
        this.filterOn = filterOn;
    }

    public MultivaluedAttribute(Map<String, String> localizedNames, List<Map<String, String>> localizedValues, Boolean filterOn) {
        this.name = localizedNames.entrySet().iterator().next().getValue();
        this.type = AttributeType.LOCALIZED_NAMES_AND_VALUES.getType();
        this.localizedNames = localizedNames;
        this.localizedValues = localizedValues;
        this.filterOn = filterOn;
    }
}



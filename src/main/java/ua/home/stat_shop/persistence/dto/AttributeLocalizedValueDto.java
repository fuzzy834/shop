package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AttributeLocalizedValueDto extends AttributeValueDto {

    @JsonProperty("localizedName")
    private Map<String, String> localeValueMap;

    public AttributeLocalizedValueDto(String id, String quantity, Map<String, String> localeValueMap) {
        super(id, quantity, true);
        this.localeValueMap = localeValueMap;
    }
}

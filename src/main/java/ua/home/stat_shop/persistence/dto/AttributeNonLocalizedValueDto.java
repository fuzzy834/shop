package ua.home.stat_shop.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeNonLocalizedValueDto extends AttributeValueDto {

    private String value;

    public AttributeNonLocalizedValueDto(String id, String quantity, String value) {
        super(id, quantity, false);
        this.value = value;
    }
}

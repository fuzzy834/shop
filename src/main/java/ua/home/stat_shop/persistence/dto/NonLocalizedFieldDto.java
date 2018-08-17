package ua.home.stat_shop.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonLocalizedFieldDto extends FieldDto {

    private String nonLocalizedField;

    public NonLocalizedFieldDto(String name) {
        super(false);
        this.nonLocalizedField = name;
    }
}

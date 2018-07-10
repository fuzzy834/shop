package ua.home.stat_shop.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonLocalizedNameDto extends NameDto {

    private String nonLocalizedName;

    public NonLocalizedNameDto(String name) {
        super(false);
        this.nonLocalizedName = name;
    }
}

package ua.home.stat_shop.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LocalizedNameDto extends NameDto {

    private Map<String, String> localizedName;

    public LocalizedNameDto(Map<String, String> localizedName) {
        super(true);
        this.localizedName = localizedName;
    }
}

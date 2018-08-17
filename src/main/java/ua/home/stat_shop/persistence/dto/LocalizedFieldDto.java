package ua.home.stat_shop.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LocalizedFieldDto extends FieldDto {

    private Map<String, String> localizedField;

    public LocalizedFieldDto(Map<String, String> localizedName) {
        super(true);
        this.localizedField = localizedName;
    }
}

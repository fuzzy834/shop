package ua.home.stat_shop.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto implements Serializable {

    String attributeId;

    String name;

    String valueId;

    String value;
}

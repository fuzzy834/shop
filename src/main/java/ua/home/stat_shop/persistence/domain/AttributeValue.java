package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttributeValue {

    private String id;

    private Integer quantity;

    private String categoryId;

    public AttributeValue() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
}

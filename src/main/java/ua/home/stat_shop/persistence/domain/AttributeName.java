package ua.home.stat_shop.persistence.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttributeName {

    private String id;

    public AttributeName() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
}

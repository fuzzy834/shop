package ua.home.stat_shop.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;
    private String name;
    private Map<String, String> localizedNames;

    public Category(String name, Map<String, String> localizedNames) {
        this.name = name;
        this.localizedNames = localizedNames;
    }
}

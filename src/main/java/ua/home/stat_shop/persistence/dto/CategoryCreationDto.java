package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.home.stat_shop.persistence.converters.deserializers.CategoryJsonDeserializer;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = CategoryJsonDeserializer.class)
public class CategoryCreationDto implements Serializable {

    private String categoryId;

    private String parent;

    private FieldDto name;

    private Set<String> attributes;
}

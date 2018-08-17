package ua.home.stat_shop.persistence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ua.home.stat_shop.persistence.domain.ProductBase;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductCreationDto implements Serializable {

    private String productId;

    private ProductBase productBase;

    private String category;

    private String videoUrl;

    private Map<String, Set<String>> attributeValueMap;

    private CommonsMultipartFile[] images;
}

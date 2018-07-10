package ua.home.stat_shop.persistence.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DTOField {

    Class[] dtoTypes();
    String refToSelf() default "";
    boolean i18n() default false;
}

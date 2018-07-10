package ua.home.stat_shop.persistence.annotations;

import org.springframework.context.ApplicationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationUtil {

    public static List<Object> findAnnotatedBeans(ApplicationContext applicationContext,
                                                  Class<? extends Annotation> annotation) {
        Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(annotation);
        return annotatedBeans.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public static List<Field> findAnnotatedFields(Class targetClass, Class<? extends Annotation> annotation) {
        return Stream.of(targetClass.getDeclaredFields()).filter(
                field -> field.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }
}

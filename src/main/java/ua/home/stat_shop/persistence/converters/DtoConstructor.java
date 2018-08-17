package ua.home.stat_shop.persistence.converters;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.stereotype.Component;
import ua.home.stat_shop.persistence.annotations.AnnotationUtil;
import ua.home.stat_shop.persistence.annotations.DTOField;
import ua.home.stat_shop.persistence.annotations.DTOType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DtoConstructor {

    private ApplicationContext applicationContext;

    @Value("#{'${available.locales}'.split(',')}")
    private List<String> availableLocales;

    private List<String> includePaths = new LinkedList<>();

    @Autowired
    public DtoConstructor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public List<String> getIncludedFields(Class dtoType) {
        Map<Class, List<Field>> annotatedMap = AnnotationUtil.findAnnotatedBeans(applicationContext, DTOType.class).stream()
                .filter(object -> {
                    Class[] dtoTypes = object.getClass().getAnnotation(DTOType.class).dtoTypes();
                    return Arrays.asList(dtoTypes).contains(dtoType);
                })
                .map(bean -> Maps.immutableEntry(bean.getClass(),
                        AnnotationUtil.findAnnotatedFields(bean.getClass(), DTOField.class).stream()
                                .filter(field -> {
                                    Class[] dtoTypes = field.getAnnotation(DTOField.class).dtoTypes();
                                    return Arrays.asList(dtoTypes).contains(dtoType);
                                })
                                .collect(Collectors.toList())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Field> fields = annotatedMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .filter(this::isFieldClosing)
                .collect(Collectors.toList());
        fields.forEach(f -> System.out.println(f.getName()));
        for (Field field : fields) {
            boolean i18n = field.getAnnotation(DTOField.class).i18n();
            if (i18n) {
                includePaths.addAll(availableLocales.stream()
                        .map(l -> getFieldPath(field, annotatedMap).concat(".").concat(l))
                        .collect(Collectors.toList()));
            } else {
                includePaths.add(getFieldPath(field, annotatedMap));
            }
        }
        return includePaths;
    }

    private String getFieldPath(Field field, Map<Class, List<Field>> classMap) {
        Class fieldDeclaringClass = field.getDeclaringClass();
        DTOType typeAnnotation = (DTOType) fieldDeclaringClass.getAnnotation(DTOType.class);
        DTOField fieldAnnotation = field.getAnnotation(DTOField.class);
        String fieldName = field.getName();
        if (field.isAnnotationPresent(Id.class)) {
            fieldName = "_" + fieldName;
        }
        if (typeAnnotation.base() && !field.getType().equals(fieldDeclaringClass)) {
            return fieldName;
        } else if (field.isAnnotationPresent(DBRef.class)) {
            return fieldName;
        } else if (field.getType().equals(fieldDeclaringClass) && !fieldAnnotation.refToSelf().isEmpty()) {
            String refToSelf = fieldAnnotation.refToSelf();
            try {
                Field refToSelfField = fieldDeclaringClass.getDeclaredField(refToSelf);
                refToSelf = refToSelfField.isAnnotationPresent(Id.class) ?
                        "_".concat(refToSelfField.getName())
                        : refToSelfField.getName();
                return fieldName.concat(".").concat(refToSelf);
            } catch (NoSuchFieldException e) {
                return fieldName.concat(".").concat(refToSelf);
            }
        } else {
            Field f;
            while (true) {
                f = getParentField(classMap.get(getParentType(fieldDeclaringClass, classMap)), fieldDeclaringClass);
                String name = f.getName();
                if (f.isAnnotationPresent(Id.class)) {
                    name = "_" + name;
                }
                fieldName = name.concat(".").concat(fieldName);
                fieldDeclaringClass = f.getDeclaringClass();
                typeAnnotation = (DTOType) fieldDeclaringClass.getAnnotation(DTOType.class);
                if (typeAnnotation.base()) {
                    break;
                }
            }
        }
        return fieldName;
    }

    private boolean isFieldClosing(Field field) {
        Class type = field.getType();
        Set<Class> basicTypes = ImmutableSet.of(String.class, Boolean.class, Character.class);
        DTOField annotation = field.getAnnotation(DTOField.class);
        if (Collection.class.isAssignableFrom(type)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class genericClass = (Class) genericType.getActualTypeArguments()[0];
            return basicTypes.contains(genericClass) || Number.class.isAssignableFrom(genericClass);
        } else {
            return type.isPrimitive()
                    || Number.class.isAssignableFrom(type)
                    || basicTypes.contains(type)
                    || (!annotation.i18n() && Map.class.isAssignableFrom(type))
                    || annotation.i18n()
                    || field.getType().equals(field.getDeclaringClass());
        }
    }

    private Field getParentField(List<Field> fields, Class fieldDeclaringClass) {
        return fields.stream()
                .filter(field -> matchFieldType(field, fieldDeclaringClass)).findFirst().orElse(null);
    }

    private Class getParentType(Class fieldDeclaringClass, Map<Class, List<Field>> classMap) {
        return classMap.keySet().stream()
                .filter(c -> classMap.get(c).stream()
                        .anyMatch(f -> matchFieldType(f, fieldDeclaringClass))).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    private boolean matchFieldType(Field field, Class fieldDeclaringClass) {
        if (field.getType().equals(fieldDeclaringClass) || field.getType().isAssignableFrom(fieldDeclaringClass)) {
            return true;
        }
        if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class genericClass = (Class) genericType.getActualTypeArguments()[0];
            return genericClass.equals(fieldDeclaringClass) || genericClass.isAssignableFrom(fieldDeclaringClass);
        }
        return false;
    }
}

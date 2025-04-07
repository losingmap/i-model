package com.lft.imodel.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldWrapper {

    private final Field field;
    private final Object entity;

    public FieldWrapper(Field field, Object entity) {
        this.field = field;
        this.entity = entity;
    }


    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    public Object getValue() {
        return ReflectionUtils.getValue(field, entity);
    }


    public String conditionalSetValue(Object value, boolean condition) {
        return ReflectionUtils.conditionalSetValue(field, value, entity, condition);
    }

    public String getName() {
        return field.getName();
    }

    public Class<?> getReturnType() {
        return field.getType();
    }
}

package com.lft.imodel.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BeanWrapper {

    private final Object entity;
    private final Class<?> clazz;

    private final List<FieldWrapper> fields = new ArrayList<>();
    private final Map<String, Method> methodMap = new HashMap<>();

    public BeanWrapper(Object entity) {
        this.entity = entity;
        clazz = entity.getClass();

        findFields(clazz);
    }

    private void findFields(Class<?> clazz) {
        if (clazz == null || clazz == Object.class)
            return;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            this.fields.add(new FieldWrapper(field, entity));
        }
        findFields(clazz.getSuperclass());
    }

    public Collection<FieldWrapper> getFields() {
        return fields;
    }

    public boolean callConditionalGetter(String name, boolean defaultValue) {
        String parameter = null;
        if (name.indexOf(' ') > -1) {
            String[] params = name.split(" ");
            name = params[0];
            parameter = params[1];
        }
        String param = parameter;
        Method conditionalMethod = methodMap.computeIfAbsent(name, n -> {
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                try {
                    if (param != null) {
                        return currentClass.getDeclaredMethod(n, String.class);
                    }
                    return currentClass.getDeclaredMethod(n);
                } catch (NoSuchMethodException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }
            return null;
        });
        if (conditionalMethod == null) {
            return defaultValue;
        }
        if (param != null) {
            return (boolean) ReflectionUtils.callMethod(conditionalMethod, new Object[]{param}, entity);
        }
        return (boolean) ReflectionUtils.callMethod(conditionalMethod, entity);
    }


}

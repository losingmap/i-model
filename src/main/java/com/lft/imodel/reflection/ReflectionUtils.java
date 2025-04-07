package com.lft.imodel.reflection;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object o) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object val;
        try {
            val = field.get(o);
            return (T) val;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unexpected error happened on get value of %s from %s",
                    field.getName(), field.getName(), o.getClass().getName()));
        } finally {
            field.setAccessible(accessible);
        }
    }

    public static String conditionalSetValue(Field field, Object value, Object entity, boolean condition) {
        if (!condition) {
            return null;
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(entity, value);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unexpected error happened on get value of %s from %s", field.getName(), value.getClass().getName()));
        } finally {
            field.setAccessible(accessible);
        }
    }

    public static Method getMethod(String methodName, Class<?>[] types, Class<?> host) {
        try {
            return host.getDeclaredMethod(methodName, types);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("error to get method %s of %s", methodName, host.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T callMethod(Method method, Object[] parameters, Object entity) {
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        try {
            return (T) method.invoke(entity, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("error to call method %s of %s", method.getName(), entity.getClass().getName()));
        } finally {
            method.setAccessible(accessible);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T callMethod(String method, Object entity) {
        return callMethod(getMethod(method, new Class[0], entity.getClass()), entity);
    }

    public static <T> T callMethod(Method method, Object entity) {
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        try {
            return (T) method.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("error to call method %s of %s", method.getName(), entity.getClass().getName()));
        } finally {
            method.setAccessible(accessible);
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(String.format("对象 %s 中不包含字段 %s", clazz.getName(), fieldName));
        }
    }
}

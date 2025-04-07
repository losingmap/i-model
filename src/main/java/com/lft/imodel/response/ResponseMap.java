package com.lft.imodel.response;

import com.lft.imodel.annotations.Flatten;
import com.lft.imodel.reflection.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ResponseMap extends LinkedHashMap<String, Object> {
    public static final String NAME_SUFFIX = "Name";

    private static final DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter simpleDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseMap() {

    }

    public ResponseMap(Object origin, ReturnHelper returnHelper) {
        this.addAll(origin, returnHelper);
    }

    public void addAll(Object origin, ReturnHelper returnHelper) {
        addAll(origin, returnHelper, true);
    }

    public Object addAll(Object origin, ReturnHelper returnHelper, boolean isOverride) {
        if (origin == null) {
            return origin;
        } else if (origin instanceof Collection) {
            Object result = returnHelper.process(origin, null);
            if (result.getClass().equals(ResponseMap.class)) {
                putAll((ResponseMap)result);
                return null;
            }
            return result;
        }
        Field[] fields = origin.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object originalValue = ReflectionUtils.getValue(field, origin);
            if (originalValue instanceof NameDisplayableEnum) {
                String displayName = ((NameDisplayableEnum)originalValue).getDisplayName();
                this.put(field.getName() + NAME_SUFFIX, displayName, isOverride);
            }
            if (originalValue instanceof LocalDate) {
                originalValue = formatDateTime((LocalDate)originalValue);
            }
            if (originalValue instanceof LocalDateTime) {
                originalValue = formatDateTime((LocalDateTime)originalValue);
            }
            if (field.isAnnotationPresent(Flatten.class)) {
                Object processedValue = addAll(originalValue, returnHelper, false);
                if (processedValue != null) {
                    this.put(field.getName(), processedValue, isOverride);
                }
                return null;
            } else {
                Object processedValue = returnHelper.process(originalValue, null);
                this.put(field.getName(), processedValue, isOverride);
            }
        }

        return null;
    }

    public Object put(String key, Object value, boolean isOverride) {
        if (isOverride) {
            return this.put(key, value);
        } else {
            return this.putIfAbsent(key, value);
        }
    }

    private String formatDateTime(LocalDate date) {
        if (date == null) {
            return null;
        }

        return simpleDateFormat.format(date);
    }

    private Object formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return simpleDateTimeFormat.format(dateTime);
    }

    @Override
    public Object put(String key, Object value) {
        // TODO: [LFT] 发布版本解除封锁
        // if (value == null) {
        //     return null;
        // }
        return super.put(key, value);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        // TODO: [LFT] 发布版本删除
        if (get(key) == null) {
            remove(key);
        }

        // TODO: [LFT] 发布版本解除封锁
        // if (value == null) {
        //     return null;
        // }
        return super.putIfAbsent(key, value);
    }

}

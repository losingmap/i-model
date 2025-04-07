package com.lft.imodel.response;

import com.lft.imodel.model.IModel;
import com.lft.imodel.reflection.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("deprecation")
public class ReturnHelper implements Closeable {

    public static final String CUSTOMIZE_MAP_METHOD_NAME = "customizeMap";

    private Object value;

    private final IModel model;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private final Map<Class<?>, Method> customizeMapMethodCache = new HashMap<>();

    public ReturnHelper(IModel model) {
        this.model = model;
        setValue(model);
    }

    public Method getCustomizeMapMethod(Class<?> clazz) {
        if (customizeMapMethodCache.containsKey(clazz)) {
            return customizeMapMethodCache.get(clazz);
        }
        Method result = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(CUSTOMIZE_MAP_METHOD_NAME)) {
                continue;
            }
            result = method;
            break;
        }
        customizeMapMethodCache.put(clazz, result);
        return result;
    }

    @Override
    public void close() {
    }


    public Object getValue() {
        if (value instanceof Responsible) {
            return ((Responsible)value).postProcess(this, null);
        }
        return value;
    }

    public void setValue(IModel value) {
        this.value = value;
    }


    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public IModel getModel() {
        return model;
    }

    public Object process(Object o, Object arg) {
        if (isValidResponseEntity(o)) {
            return processValue(o, arg);
        } else if (o instanceof Collection) {
            return processCollection((Collection<?>)o, arg);
        } else if (o != null && o.getClass().isArray() && isValidResponseEntity(o.getClass().getComponentType())) {
            return processArray((Object[])o, arg);
        }
        return o;
    }

    private void invokeCustomizeMap(ResponseMap map, Object entity, Object arg) {
        Method method = getCustomizeMapMethod(entity.getClass());
        if (method == null) {
            return;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (ResponseMap.class.isAssignableFrom(type)) {
                parameters[i] = map;
            } else if (ReturnHelper.class.isAssignableFrom(type)) {
                parameters[i] = this;
            } else if (arg != null && arg.getClass().isAssignableFrom(type)) {
                parameters[i] = arg;
            } else {
                parameters[i] = null;
            }
        }
        ReflectionUtils.callMethod(method, parameters, entity);
    }

    private Object processArray(Object[] arr, Object arg) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = processValue(arr[i], arg);
        }
        return arr;
    }

    private Object processCollection(Collection<?> collection, Object arg) {
        if (collection.isEmpty()) {
            return collection;
        }

        Optional<?> first = collection.stream().findFirst();
        if (!(isValidResponseEntity(first.get()))) {
            return collection;
        }

        List<Object> response = new ArrayList<>();
        for (Object o : collection) {
            if (isValidResponseEntity(o)) {
                response.add(processValue(o, arg));
            } else {
                response.add(o);
            }
        }
        return response;
    }

    private Object processValue(Object entity, Object arg) {
        if (entity instanceof Responsible) {
            return ((Responsible)entity).postProcess(this, arg);
        }

        return convertToMap(entity, arg);
    }

    public ResponseMap convertToMap(Object entity, Object arg) {
        ResponseMap map = new ResponseMap(entity, this);
        invokeCustomizeMap(map, entity, arg);
        return map;
    }

    private boolean isValidResponseEntity(Object o) {
        return o != null && isValidResponseEntity(o.getClass());
    }

    private boolean isValidResponseEntity(Class<?> clazz) {
        return clazz.isAnnotationPresent(ResponseEntity.class) || Responsible.class.isAssignableFrom(clazz);
    }

}

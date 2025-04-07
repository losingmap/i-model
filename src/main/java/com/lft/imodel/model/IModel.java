package com.lft.imodel.model;


import com.lft.imodel.exception.CustomFailureException;
import com.lft.imodel.exception.ViewChangeException;
import com.lft.imodel.handler.CustomFailureHandler;
import com.lft.imodel.handler.ValidationFailureHandler;

import java.util.Set;

public interface IModel {
    Set<String> keySet();

    Object put(String key, Object value);

    Object get(Object key);

    default IModel add(String key, Object value) {
        put(key, value);
        return this;
    }

    default IModel code(Integer code) {
        put("code", code);
        return this;
    }

    default IModel err(String msg) {
        code(500);
        put("errMsg", msg);
        return this;
    }

    default IModel info(String msg) {
        code(200);
        put("infoMsg", msg);
        return this;
    }

    default IModel warn(String msg) {
        code(200);
        put("warnMsg", msg);
        return this;
    }
    default IModel success() {
        code(200);
        return this;
    }

    default void failure(String message) throws CustomFailureException {
        code(500);
        throw new CustomFailureException(message);
    }

    void valid(boolean valid);

    boolean valid();

    void redirectTo(String viewName) throws ViewChangeException;

    void forwardTo(String viewName) throws ViewChangeException;

    String currentPath();

    void invalidCallback(ValidationFailureHandler callback);

    void failureCallback(CustomFailureHandler callback);

    CustomFailureHandler failureCallback();

    ValidationFailureHandler validationFailureDelegate();

    void reclaim();
}

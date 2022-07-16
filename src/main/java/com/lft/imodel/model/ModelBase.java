package com.lft.imodel.model;

import com.lft.imodel.exception.InvalidEntityException;
import com.lft.imodel.exception.ViewChangeException;
import com.lft.imodel.handler.ValidationFailureHandler;
import com.lft.imodel.handler.CustomFailureHandler;

import java.util.HashMap;

public abstract class ModelBase extends HashMap<String, Object> implements IModel {
    private CustomFailureHandler failureDelegate;
    private ValidationFailureHandler invalidDelegate;
    private ValidationFailureHandler validationFailureDelegate;
    protected String path = null;
    private boolean valid = true;


    @Override
    public void reclaim() {
        // TODO: pooledObject
        valid = true;
        path = null;
        failureDelegate = null;
        invalidDelegate = null;
        validationFailureDelegate = null;
    }

    @Override
    public void valid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean valid() {
        return this.valid;
    }

    @Override
    public void redirectTo(String path) throws ViewChangeException {
        this.path = "redirect:" + path;
        throw new ViewChangeException();
    }

    @Override
    public void forwardTo(String routeName) throws ViewChangeException {
        this.path = routeName;
        throw new ViewChangeException();
    }

    @Override
    public String currentPath() {
        return this.path;
    }

    public CustomFailureHandler getFailureDelegate() {
        return failureDelegate;
    }

    public void setFailureDelegate(CustomFailureHandler failureDelegate) {
        this.failureDelegate = failureDelegate;
    }

    public ValidationFailureHandler getInvalidDelegate() {
        return invalidDelegate;
    }

    public void setInvalidDelegate(ValidationFailureHandler invalidDelegate) {
        this.invalidDelegate = invalidDelegate;
    }

    @Override
    public void invalidCallback(ValidationFailureHandler callback) {
        if (valid || validationFailureDelegate != null)
            return;

        validationFailureDelegate = callback;
        throw new InvalidEntityException("[failure validate entity] invalid parameters");
    }

    @Override
    public void failureCallback(CustomFailureHandler callback) {
        failureDelegate = callback;
    }

    @Override
    public CustomFailureHandler failureCallback() {
        return failureDelegate;
    }

    @Override
    public ValidationFailureHandler validationFailureDelegate() {
        return validationFailureDelegate;
    }
}

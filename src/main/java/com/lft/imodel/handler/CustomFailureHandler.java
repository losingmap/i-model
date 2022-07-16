package com.lft.imodel.handler;

import com.lft.imodel.exception.CustomFailureException;
import com.lft.imodel.exception.ValidationFailureException;
import com.lft.imodel.exception.ViewChangeException;

public interface CustomFailureHandler {
    void invoke(CustomFailureException message) throws Throwable;
}

package com.lft.imodel.handler;

import com.lft.imodel.exception.InvalidEntityException;
import com.lft.imodel.exception.ViewChangeException;

public interface ValidationFailureHandler {
    void invoke(InvalidEntityException message) throws Throwable;
}

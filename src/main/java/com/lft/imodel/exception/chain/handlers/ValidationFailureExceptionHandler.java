package com.lft.imodel.exception.chain.handlers;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.ValidationFailureException;
import com.lft.imodel.exception.chain.ExceptionHandler;
import com.lft.imodel.exception.chain.ExceptionResolvingChain;

public class ValidationFailureExceptionHandler implements ExceptionHandler<ValidationFailureException> {
    @Override
    public boolean canHandle(Throwable e) {
        return e instanceof ValidationFailureException;
    }

    @Override
    public void handle(ExceptionResolvingChain chain, ReturnWrap wrap, ValidationFailureException e) throws Throwable{

    }
}

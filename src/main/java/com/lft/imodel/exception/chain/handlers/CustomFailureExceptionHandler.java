package com.lft.imodel.exception.chain.handlers;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.CustomFailureException;
import com.lft.imodel.exception.chain.ExceptionHandler;
import com.lft.imodel.exception.chain.ExceptionResolvingChain;

public class CustomFailureExceptionHandler implements ExceptionHandler<CustomFailureException> {

    @Override
    public boolean canHandle(Throwable e) {
        return e instanceof CustomFailureException;
    }

    @Override
    public void handle(ExceptionResolvingChain chain, ReturnWrap wrap, CustomFailureException e) throws Throwable {
        try {
            wrap.getModel().failureCallback().invoke(e);
        } catch (CustomFailureException ignore) {

        } catch (Throwable ex) {
            chain.resolve(wrap, ex);
        }
    }
}

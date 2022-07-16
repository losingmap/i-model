package com.lft.imodel.exception.chain;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.chain.handlers.CustomFailureExceptionHandler;
import com.lft.imodel.exception.chain.handlers.InvalidEntityExceptionHandler;
import com.lft.imodel.exception.chain.handlers.ValidationFailureExceptionHandler;
import com.lft.imodel.exception.chain.handlers.ViewChangeExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class ExceptionResolvingChain {

    private List<ExceptionHandler<? extends Throwable>> handlers;


    public ExceptionResolvingChain() {
        handlers = new ArrayList<>();
        handlers.add(new CustomFailureExceptionHandler());
        handlers.add(new InvalidEntityExceptionHandler());
        handlers.add(new ValidationFailureExceptionHandler());
        handlers.add(new ViewChangeExceptionHandler());
    }

    public void resolve(ReturnWrap wrap, Throwable e) throws Throwable {
        for (; ; ) {
            try {
                for (ExceptionHandler handler : handlers) {
                    if (handler.canHandle(e)) {
                        handler.handle(this, wrap, e);
                        return;
                    }
                }
                break;
            } catch (Throwable ex) {
                e = ex;
            }
        }

        throw e;
    }
}

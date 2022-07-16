package com.lft.imodel.exception.chain;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.InvalidEntityException;

public interface ExceptionHandler<T extends Throwable> {
    boolean canHandle(Throwable e);

    void handle(ExceptionResolvingChain chain, ReturnWrap wrap, T e) throws Throwable;
}

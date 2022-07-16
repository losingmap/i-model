package com.lft.imodel.exception.chain.handlers;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.InvalidEntityException;
import com.lft.imodel.exception.chain.ExceptionHandler;
import com.lft.imodel.exception.chain.ExceptionResolvingChain;
import com.lft.imodel.model.IModel;

public class InvalidEntityExceptionHandler implements ExceptionHandler<InvalidEntityException> {
    @Override
    public boolean canHandle(Throwable e) {
        return e instanceof InvalidEntityException;
    }

    @Override
    public void handle(ExceptionResolvingChain chain, ReturnWrap wrap, InvalidEntityException e) throws Throwable {
        IModel model = wrap.getModel();
        if (wrap.isRest()) {
            wrap.setValue(model);
        }
        model.validationFailureDelegate().invoke(e);
    }
}

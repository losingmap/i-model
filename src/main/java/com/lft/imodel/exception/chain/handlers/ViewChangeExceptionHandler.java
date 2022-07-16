package com.lft.imodel.exception.chain.handlers;

import com.lft.imodel.aop.ReturnWrap;
import com.lft.imodel.exception.RouteNotFountException;
import com.lft.imodel.exception.ViewChangeException;
import com.lft.imodel.exception.chain.ExceptionHandler;
import com.lft.imodel.exception.chain.ExceptionResolvingChain;

import java.io.IOException;

public class ViewChangeExceptionHandler implements ExceptionHandler<ViewChangeException> {
    @Override
    public boolean canHandle(Throwable e) {
        return e instanceof ViewChangeException;
    }

    @Override
    public void handle(ExceptionResolvingChain chain, ReturnWrap wrap, ViewChangeException e) throws Throwable {
        String path = wrap.getModel().currentPath();
        if (wrap.isRest()) {
            if (path == null) {
                throw new RouteNotFountException("cannot route to null path");
            }
            if (path.startsWith("redirect:")) {
                wrap.getResponse().sendRedirect(path.substring(9));
            }
            wrap.setValue(null);
        } else {
            wrap.setValue(path);
        }
    }
}

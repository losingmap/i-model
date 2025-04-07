package com.lft.imodel.aop;


import com.lft.imodel.exception.chain.ExceptionResolvingChain;
import com.lft.imodel.response.IModelMessageConverterHandler;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Aspect
public class ControllerIntercept {

    private final ExceptionResolvingChain chain = new ExceptionResolvingChain();

    public ControllerIntercept(
            RequestMappingHandlerAdapter adapter
    ) {
        this.adaptMMessageConverterHandler(adapter);
    }

    private void adaptMMessageConverterHandler(RequestMappingHandlerAdapter adapter) {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();

        ArrayList<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers == null ? Collections.emptyList() : returnValueHandlers);
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(argumentResolvers == null ? Collections.emptyList() : argumentResolvers);

        IModelMessageConverterHandler proxy = null;
        for (int i = 0; i < handlers.size(); i++) {
            HandlerMethodReturnValueHandler handler = handlers.get(i);
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                proxy = new IModelMessageConverterHandler((RequestResponseBodyMethodProcessor)handler);
                handlers.set(i, proxy);
                break;
            }
        }

        for (int i = 0; i < resolvers.size(); i++) {
            HandlerMethodArgumentResolver resolver = resolvers.get(i);
            if (resolver instanceof RequestResponseBodyMethodProcessor) {
                resolvers.set(i, proxy);
            }
        }

        adapter.setReturnValueHandlers(handlers);
        adapter.setArgumentResolvers(resolvers);
    }

}

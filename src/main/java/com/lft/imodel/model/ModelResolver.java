package com.lft.imodel.model;

import com.lft.imodel.factory.ModelFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ModelResolver implements HandlerMethodArgumentResolver {
    private final ModelFactory factory;

    public ModelResolver(ModelFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(IModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Method method = methodParameter.getMethod();
        if (method == null)
            return null;

        return factory.get(method);
    }
}

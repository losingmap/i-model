package com.lft.imodel.response;

import com.lft.imodel.annotations.IModelController;
import com.znzz.common.model.R;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

public final class IModelMessageConverterHandler implements HandlerMethodReturnValueHandler, HandlerMethodArgumentResolver {

    private final RequestResponseBodyMethodProcessor handler;

    public IModelMessageConverterHandler(RequestResponseBodyMethodProcessor handler) {
        this.handler = handler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return handler.supportsReturnType(methodParameter);
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        if (!AnnotatedElementUtils.hasAnnotation(methodParameter.getDeclaringClass(), IModelController.class)) {
            handler.handleReturnValue(o, methodParameter, modelAndViewContainer, nativeWebRequest);
            return;
        }

        if (o == null) {
            this.handler.handleReturnValue(R.ok(), methodParameter, modelAndViewContainer, nativeWebRequest);
            return;
        }

        try (ReturnHelper helper = new ReturnHelper(null)) {
            if (o.getClass().equals(R.class)) {
                this.handler.handleReturnValue(helper.process(o, null), methodParameter, modelAndViewContainer, nativeWebRequest);
                return;
            }
            this.handler.handleReturnValue(helper.process(R.ok(o), null), methodParameter, modelAndViewContainer, nativeWebRequest);
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return handler.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return handler.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}

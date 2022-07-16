package com.lft.imodel.factory;

import com.lft.imodel.model.IModel;
import com.lft.imodel.model.RestModel;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

public class RestModelFactoryBean implements IModelFactoryBean {
    @Override
    public IModel tryGet(Method method) {
        boolean isRestful = method.getDeclaringClass().isAnnotationPresent(RestController.class) ||
                method.getDeclaringClass().isAnnotationPresent(ResponseBody.class) ||
                method.isAnnotationPresent(ResponseBody.class);

        if (!isRestful)
            return null;

        return new RestModel();
    }
}

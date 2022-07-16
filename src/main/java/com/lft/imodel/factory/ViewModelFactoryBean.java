package com.lft.imodel.factory;

import com.lft.imodel.model.IModel;
import com.lft.imodel.model.RestModel;
import com.lft.imodel.model.ViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

public class ViewModelFactoryBean implements IModelFactoryBean {
    @Override
    public IModel tryGet(Method method) {
        boolean isController = method.getDeclaringClass().isAnnotationPresent(Controller.class);
        if (!isController)
            return null;

        return new ViewModel();
    }
}

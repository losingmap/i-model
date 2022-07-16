package com.lft.imodel.factory;

import com.lft.imodel.model.IModel;

import java.lang.reflect.Method;

public interface IModelFactoryBean {
    IModel tryGet(Method method);
}

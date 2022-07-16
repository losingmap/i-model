package com.lft.imodel.factory;

import com.lft.imodel.model.IModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModelFactory {
    private final List<IModelFactoryBean> factories = new ArrayList<>();

    public ModelFactory() {
        register(new RestModelFactoryBean());
        register(new ViewModelFactoryBean());
    }
    
    public IModel get(Method method) {
        for (IModelFactoryBean factory : factories) {
            IModel imodel = factory.tryGet(method);
            if (imodel != null)
                return imodel;
        }

        return null;
    }

    public void register(IModelFactoryBean bean) {
        factories.add(bean);
    }

    public void register(int index,IModelFactoryBean bean ) {
        factories.add(index, bean);
    }
}

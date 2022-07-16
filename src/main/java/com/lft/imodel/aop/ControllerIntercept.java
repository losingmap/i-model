package com.lft.imodel.aop;


import com.lft.imodel.entity.ValidationEntity;
import com.lft.imodel.exception.CustomFailureException;
import com.lft.imodel.exception.InvalidEntityException;
import com.lft.imodel.exception.ViewChangeException;
import com.lft.imodel.exception.chain.ExceptionResolvingChain;
import com.lft.imodel.handler.CustomFailureHandler;
import com.lft.imodel.model.IModel;
import com.lft.imodel.model.RestModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
public class ControllerIntercept {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    private final ExceptionResolvingChain chain = new ExceptionResolvingChain();

    public ControllerIntercept(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void getPointcut() {
    }

    @Around("getPointcut()")
    public Object logAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        IModel model = null;
        try {
            ModelAndView mv = null;
            boolean valid = true;
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (valid && arg instanceof ValidationEntity) {
                    ValidationEntity entity = (ValidationEntity) arg;
                    if (!entity.valid()) {
                        valid = false;
                    }
                } else if (arg instanceof IModel) {
                    model = (IModel) arg;
                }
            }

            if (model == null) {
                return joinPoint.proceed();
            }

            model.valid(valid);
            boolean isRest = model instanceof RestModel;

            ReturnWrap wrap = new ReturnWrap();
            wrap.setRest(isRest);
            wrap.setModel(model);
            wrap.setRequest(req);
            wrap.setResponse(resp);
            try {
                wrap.setValue(joinPoint.proceed());
            } catch (Exception e) {
                chain.resolve(wrap, e);
            }

            if (isRest) {
                wrap.setValue(model);
            } else if (model.currentPath() != null) {
                for (String key : model.keySet()) {
                    req.setAttribute(key, model.get(key));
                }
            }

            return wrap.getValue();
        } finally {
            if (model != null)
                model.reclaim();
        }
    }
}

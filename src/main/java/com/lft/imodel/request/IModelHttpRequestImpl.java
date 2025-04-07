package com.lft.imodel.request;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public class IModelHttpRequestImpl implements IModelHttpRequest {

    private final HttpServletRequest request;

    private final boolean isPatch;

    public IModelHttpRequestImpl(HttpServletRequest request) {
        this.request = request;
        isPatch = HttpMethod.PATCH.name().equals(request.getMethod());
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }


    @Override
    public boolean isPatch() {
        return isPatch;
    }
}

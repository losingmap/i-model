package com.lft.imodel.request;

import javax.servlet.http.HttpServletRequest;

public interface IModelHttpRequest {

    HttpServletRequest getRequest();

    boolean isPatch();
}

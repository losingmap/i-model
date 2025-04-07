package com.lft.imodel.response;

public interface Responsible {
    default Object postProcess(ReturnHelper returnHelper, Object arg) {
        return this;
    }
}

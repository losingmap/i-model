package com.lft.imodel.response;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/*
 编写以下方法来扩展返回的ResponseMap,参数可选
 private void customizeMap(ResponseMap map, ReturnHelper helper) {

 }
 */
public @interface ResponseEntity {
}

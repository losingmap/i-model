package com.lft.imodel.enums;

public enum RespCode {
    SUCCESS(200, "成功"),
    FAILURE(400, "失败"),
    AUTH_NEEDED(401, "请登录"),
    FORBIDDEN(403, "无权访问"),
    ERROR(500, "异常"),
    ;

    public final int code;

    public final String message;

    RespCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

/**
 * 
 */
package com.github.jessyZu.jsongood.core;


public enum RpcResultCodeEnum {
    SUCCESS(1000, "success"),
    SYSTEM_ERROR(-1000, "system error"),
    DECODE_ERROR(-1001, "decoded rpc request error"),
    METHOD_ENDPOINT_ERROR(-1002, "methodEndPoint format error,splitMethodEndPoint length must be 2 or 3"),
    CLASS_NOT_FOUND_ERROR(-1003, "can't find a service class with the classname"),
    SERVICE_BEAN_NOT_FOUND_ERROR(-1004, "can't find a service bean with the classname"),
    METHOD_NOT_FOUND_ERROR(-1005, "can't find a mehotd with the service bean"),
    METHOD_INVOKE_ERROR(-1006, "invoke method occurs error"),
    ENCODE_ERROR(-1007, "encoded rpc result error"),
    PARAMETER_ERROR(-1008, " rpc request parameter error"),
    VALIDATION_ERROR(-1009, "method  validation error");

    private int    code;
    private String message;

    private RpcResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

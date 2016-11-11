/**
 *
 */
package com.github.jessyZu.jsongood.core;

import java.util.Map;


public class RpcContext {

    private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    /**
     * get context.
     *
     * @return context
     */
    public static RpcContext getContext() {
        return LOCAL.get();
    }

    /**
     * remove context.
     */
    public static void removeContext() {
        LOCAL.remove();
    }

    private RpcRequest rpcRequest;
    private String jsonpCallback;
    private String className;
    private String methodName;
    private String serviceVersion;


    private Map<String, String> attachments;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }


    public RpcRequest getRpcRequest() {
        return rpcRequest;
    }

    public void setRpcRequest(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }

    public String getJsonpCallback() {
        return jsonpCallback;
    }

    public void setJsonpCallback(String jsonpCallback) {
        this.jsonpCallback = jsonpCallback;
    }

    public boolean isJSONPRequest() {
        return this.jsonpCallback != null;
    }

    @Override
    public String toString() {
        return "RpcContext [" + (rpcRequest != null ? "rpcRequest=" + rpcRequest + ", " : "")
                + (jsonpCallback != null ? "jsonpCallback=" + jsonpCallback + ", " : "")
                + (className != null ? "className=" + className + ", " : "")
                + (methodName != null ? "methodName=" + methodName + ", " : "")
                + (serviceVersion != null ? "serviceVersion=" + serviceVersion + ", " : "")
                + (attachments != null ? "attachments=" + attachments : "") + "]";
    }

}

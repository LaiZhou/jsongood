/**
 * 
 */
package com.github.jessyZu.jsongood.core;

import java.util.ArrayList;
import java.util.List;
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

    private RpcRequest          rpcRequest;
    private String              jsonpCallback;
    private String              className;
    private String              methodName;
    private String              serviceVersion;

    private List<Class<?>>      parameterTypes = new ArrayList<Class<?>>();
    private List<String>        parameters     = new ArrayList<String>();

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

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Class<?>> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
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
                + (parameterTypes != null ? "parameterTypes=" + parameterTypes + ", " : "")
                + (parameters != null ? "parameters=" + parameters + ", " : "")
                + (attachments != null ? "attachments=" + attachments : "") + "]";
    }

}
